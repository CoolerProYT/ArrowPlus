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

function registry(kind) {
  return namespaces.flatMap((namespace) =>
    jsonFiles(join(generated, namespace, 'arrowplus', kind)).map((file) => ({
      id: `${namespace}:${basename(file, '.json')}`,
      json: readJson(join(generated, namespace, 'arrowplus', kind, file)),
    })),
  )
}

const VANILLA_STICK = 0xff886627 | 0
const VANILLA_FEATHER = -1
const STICK = { gravity: 0.05, velocity: 1.0, outputAmount: 4, standalone: false }
const FEATHER = { gravityMultiplier: 1.0, airDrag: 0.99, waterDrag: 0.6, accuracy: 1.0, outputAmount: 4, standalone: false }

const variant = (id, suffix) => `${id}_${suffix}`
const names = { 'minecraft:arrow': lang['item.minecraft.arrow'] }
const textures = {}
function addItem(item, translationKey, layers) {
  names[item] = lang[translationKey] ?? translationKey
  if (layers) textures[item] = { layers }
}

function part(id, json, defaults, suffix, layer) {
  const entry = { ...defaults, ...json }
  const material = list(json.material)
  const item = entry.standalone ? material[0] : variant(id, suffix)
  if (!entry.standalone) addItem(item, json.translationKey, [[layer, json.color]])
  const { translationKey, ...stats } = entry
  return { id, item, ...stats, material }
}

const sticks = registry('sticks').map(({ id, json }) => part(id, json, STICK, 'stick', 'stick'))
const feathers = registry('feathers').map(({ id, json }) => part(id, json, FEATHER, 'feather', 'feather'))

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
  addItem(item, json.translationKey, [
    ['arrow_stick', VANILLA_STICK],
    ['arrow_head', json.color],
    ['arrow_feather', VANILLA_FEATHER],
  ])
  return {
    id,
    item,
    material: typeof json.material === 'string' ? json.material : list(json.material)[0],
    outputAmount: json.outputAmount ?? 4,
    fletchingAmount: fletching[id] ?? null,
    baseDamage: json.baseDamage,
    flame: json.flame,
    effects: json.effects ?? {},
    color: json.color,
  }
})

const POISON = 0xff87a363 | 0
const diamond = arrows.find((a) => a.id === 'arrowplus:diamond')
if (diamond) {
  textures['arrowplus:diamond_arrow_tipped'] = {
    layers: [...textures[diamond.item].layers, ['arrow_tipped', POISON]],
  }
}

const layers = join(root, 'common/src/main/resources/assets/arrowplus/textures/item')
mkdirSync(join(docs, 'public/layers'), { recursive: true })
for (const name of ['arrow_stick', 'arrow_head', 'arrow_feather', 'arrow_tipped', 'stick', 'feather']) {
  copyFileSync(join(layers, `${name}.png`), join(docs, `public/layers/${name}.png`))
}

mkdirSync(join(docs, '.vitepress/data'), { recursive: true })
writeFileSync(join(docs, '.vitepress/data/data.json'), JSON.stringify({ names, textures, arrows, sticks, feathers }, null, 2))
console.log(`Synced ${arrows.length} heads, ${sticks.length} sticks, ${feathers.length} feathers.`)
