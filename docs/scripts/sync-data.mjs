// Pulls wiki data straight from the mod so the docs never drift from the game:
// the arrow, stick and feather datapack entries, Fletching Table recipes and names from the lang file.
// Run `./gradlew :fabric:runDatagen` first when the mod's built-in arrows change.
import { copyFileSync, existsSync, mkdirSync, readdirSync, readFileSync, writeFileSync } from 'node:fs'
import { basename, dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'

const docs = join(dirname(fileURLToPath(import.meta.url)), '..')
const root = join(docs, '..')
const generated = join(root, 'fabric/src/main/generated/data')
const lang = JSON.parse(readFileSync(join(root, 'common/src/main/resources/assets/arrowplus/lang/en_us.json'), 'utf8'))

if (!existsSync(generated)) {
  console.error(`No datagen output at ${generated}. Run ./gradlew :fabric:runDatagen first.`)
  process.exit(1)
}

const readJson = (file) => JSON.parse(readFileSync(file, 'utf8'))
const jsonFiles = (dir) => (existsSync(dir) ? readdirSync(dir).filter((f) => f.endsWith('.json')).sort() : [])
const list = (value) => (value == null ? [] : Array.isArray(value) ? value : [value])
const namespaces = readdirSync(generated).sort()

/** Every entry of one Arrow+ datapack registry, across namespaces, keyed `namespace:name`. */
function registry(kind) {
  return namespaces.flatMap((namespace) =>
    jsonFiles(join(generated, namespace, 'arrowplus', kind)).map((file) => ({
      id: `${namespace}:${basename(file, '.json')}`,
      json: readJson(join(generated, namespace, 'arrowplus', kind, file)),
    })),
  )
}

// Each stick, feather and arrow variant is shown as its own item, e.g. arrowplus:copper_stick,
// with the icon rendered by render-icons.py and hosted alongside the vanilla renders.
const HOSTED_TEXTURES = 'https://storage.googleapis.com/coolerpromc/textures'
const variant = (id, suffix) => `${id}_${suffix}`
const names = { 'minecraft:arrow': lang['item.minecraft.arrow'] }
const textures = {}
function addItem(item, translationKey) {
  names[item] = lang[translationKey] ?? translationKey
  const [namespace, path] = item.split(':')
  textures[item] = `${HOSTED_TEXTURES}/${namespace}/${path}.png`
}

const sticks = registry('sticks').map(({ id, json }) => {
  const item = variant(id, 'stick')
  addItem(item, json.translationKey)
  return { id, item, material: list(json.material), color: json.color, outputAmount: json.outputAmount ?? 4 }
})

const feathers = registry('feathers').map(({ id, json }) => {
  const item = variant(id, 'feather')
  addItem(item, json.translationKey)
  return { id, item, material: list(json.material), color: json.color, outputAmount: json.outputAmount ?? 4 }
})

/** A stick or feather slot: the vanilla item, or the custom item's variant. */
const part = (itemId, dataId, suffix) => (dataId ? variant(dataId, suffix) : itemId)

const fletching = Object.fromEntries(
  namespaces.flatMap((namespace) =>
    jsonFiles(join(generated, namespace, 'recipe/fletching')).map((file) => {
      const json = readJson(join(generated, namespace, 'recipe/fletching', file))
      return [json.output?.components?.['arrowplus:arrow_data'], json.output?.count ?? 0]
    }),
  ),
)

const arrows = registry('arrows').map(({ id, json }) => {
  const item = variant(id, 'arrow')
  addItem(item, json.translationKey)
  return {
    id,
    item,
    material: typeof json.material === 'string' ? json.material : list(json.material)[0],
    stick: part(json.stick, json.stickData, 'stick'),
    feather: part(json.feather, json.featherData, 'feather'),
    outputAmount: json.outputAmount,
    fletchingAmount: fletching[id] ?? null,
    baseDamage: json.baseDamage,
    flame: json.flame,
    gravity: json.gravity,
    effects: json.effects ?? {},
    color: json.color,
  }
})

// The untinted texture layers, for the arrow designer's live preview.
const layers = join(root, 'common/src/main/resources/assets/arrowplus/textures/item')
mkdirSync(join(docs, 'public/layers'), { recursive: true })
for (const name of ['arrow_stick', 'arrow_head', 'arrow_feather']) {
  copyFileSync(join(layers, `${name}.png`), join(docs, `public/layers/${name}.png`))
}

mkdirSync(join(docs, '.vitepress/data'), { recursive: true })
writeFileSync(join(docs, '.vitepress/data/data.json'), JSON.stringify({ names, textures, arrows, sticks, feathers }, null, 2))
console.log(`Synced ${arrows.length} arrows, ${sticks.length} sticks, ${feathers.length} feathers.`)
