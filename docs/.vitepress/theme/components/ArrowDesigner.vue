<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { withBase } from 'vitepress'
import { data, hex, itemName } from '../arrowplus'
import ItemSlot from './ItemSlot.vue'

const VANILLA_STICK = 0xff886627
const VANILLA_FEATHER = 0xffffffff

const id = ref('mymod:ruby')
const displayName = ref('Ruby Arrow')
const material = ref('minecraft:redstone_block')
const damage = ref(3)
const colour = ref('#E0115F')
const stick = ref('')
const feather = ref('')
const output = ref(4)
const flame = ref(false)
const gravity = ref(0.05)

const namespace = computed(() => (id.value.includes(':') ? id.value.split(':')[0] : '') || 'mymod')
const path = computed(() => id.value.split(':').pop() || 'arrow')
const translationKey = computed(() => `item.${namespace.value}.${path.value}_arrow`)

/** The signed ARGB int the datapack wants, from a #RRGGBB color. */
const colourInt = computed(() => (0xff000000 | parseInt(colour.value.slice(1), 16)) | 0)

const stickPart = computed(() => data.sticks.find((s) => s.id === stick.value))
const featherPart = computed(() => data.feathers.find((f) => f.id === feather.value))

const json = computed(() => {
  const result: Record<string, unknown> = {
    material: material.value,
    baseDamage: Number(damage.value),
    color: colourInt.value,
    translationKey: translationKey.value,
    flame: flame.value,
    gravity: Number(gravity.value),
    effects: {},
    feather: featherPart.value ? 'arrowplus:custom_feather' : 'minecraft:feather',
  }
  if (featherPart.value) result.featherData = featherPart.value.id
  result.stick = stickPart.value ? 'arrowplus:custom_stick' : 'minecraft:stick'
  if (stickPart.value) result.stickData = stickPart.value.id
  result.outputAmount = Number(output.value)
  return JSON.stringify(result, null, 2)
})
const langJson = computed(() => JSON.stringify({ [translationKey.value]: displayName.value }, null, 2))

// Live preview: the mod's untinted layers, multiplied by the chosen colors like the game's tint sources.
const canvas = ref<HTMLCanvasElement>()
const images: Record<string, HTMLImageElement> = {}
const loaded = ref(false)

function load(name: string) {
  return new Promise<void>((resolve) => {
    const image = new Image()
    image.onload = () => resolve()
    image.onerror = () => resolve()
    image.src = withBase(`/layers/${name}.png`)
    images[name] = image
  })
}

function draw() {
  const ctx = canvas.value?.getContext('2d')
  if (!ctx || !loaded.value) return
  ctx.clearRect(0, 0, 16, 16)
  const layers: [string, number][] = [
    ['arrow_stick', stickPart.value?.color ?? VANILLA_STICK],
    ['arrow_head', colourInt.value],
    ['arrow_feather', featherPart.value?.color ?? VANILLA_FEATHER],
  ]
  const scratch = document.createElement('canvas')
  scratch.width = scratch.height = 16
  const sctx = scratch.getContext('2d')!
  for (const [name, argb] of layers) {
    const image = images[name]
    if (!image?.naturalWidth) continue
    sctx.clearRect(0, 0, 16, 16)
    sctx.drawImage(image, 0, 0)
    const pixels = sctx.getImageData(0, 0, 16, 16)
    const [r, g, b] = [(argb >>> 16) & 255, (argb >>> 8) & 255, argb & 255]
    for (let i = 0; i < pixels.data.length; i += 4) {
      pixels.data[i] = (pixels.data[i] * r) / 255
      pixels.data[i + 1] = (pixels.data[i + 1] * g) / 255
      pixels.data[i + 2] = (pixels.data[i + 2] * b) / 255
    }
    sctx.putImageData(pixels, 0, 0)
    ctx.drawImage(scratch, 0, 0)
  }
}

onMounted(async () => {
  await Promise.all(['arrow_stick', 'arrow_head', 'arrow_feather'].map(load))
  loaded.value = true
  draw()
})
watch([colourInt, stickPart, featherPart], draw)

const copied = ref('')
async function copy(text: string, what: string) {
  try {
    await navigator.clipboard.writeText(text)
    copied.value = what
    setTimeout(() => (copied.value = ''), 1500)
  } catch {
    // Clipboard blocked: the code stays selectable.
  }
}
</script>

<template>
  <div class="ap-designer">
    <div class="form">
      <label>
        <span>Arrow id</span>
        <input v-model.trim="id" spellcheck="false" />
      </label>
      <label>
        <span>Display name</span>
        <input v-model="displayName" />
      </label>
      <label>
        <span>Material</span>
        <input v-model.trim="material" spellcheck="false" placeholder="minecraft:diamond or #minecraft:planks" />
      </label>
      <label>
        <span>Head color</span>
        <span class="colour">
          <input v-model="colour" type="color" />
          <code>{{ hex(colourInt) }} → {{ colourInt }}</code>
        </span>
      </label>
      <label>
        <span>Stick</span>
        <select v-model="stick">
          <option value="">Stick (vanilla)</option>
          <option v-for="s in data.sticks" :key="s.id" :value="s.id">{{ itemName(s.item) }}</option>
        </select>
      </label>
      <label>
        <span>Feather</span>
        <select v-model="feather">
          <option value="">Feather (vanilla)</option>
          <option v-for="f in data.feathers" :key="f.id" :value="f.id">{{ itemName(f.item) }}</option>
        </select>
      </label>
      <div class="numbers">
        <label>
          <span>Base damage</span>
          <input v-model.number="damage" type="number" step="0.1" min="0" />
        </label>
        <label>
          <span>Output</span>
          <input v-model.number="output" type="number" step="1" min="1" max="64" />
        </label>
        <label>
          <span>Gravity</span>
          <input v-model.number="gravity" type="number" step="0.01" min="0" />
        </label>
      </div>
      <label class="check"><input v-model="flame" type="checkbox" /> Sets targets on fire</label>
    </div>

    <div class="out">
      <div class="preview">
        <span class="slot"><canvas ref="canvas" width="16" height="16" class="pixelated" /></span>
        <div>
          <div class="name">{{ displayName || 'Unnamed Arrow' }}</div>
          <div class="ap-muted">
            <ItemSlot :id="material" /> + <ItemSlot :id="stickPart?.item ?? 'minecraft:stick'" /> +
            <ItemSlot :id="featherPart?.item ?? 'minecraft:feather'" /> → ×{{ output }}
          </div>
        </div>
      </div>

      <div class="file">
        <div class="file-head">
          <code>data/{{ namespace }}/arrowplus/arrows/{{ path }}.json</code>
          <button @click="copy(json, 'json')">{{ copied === 'json' ? 'Copied' : 'Copy' }}</button>
        </div>
        <pre><code>{{ json }}</code></pre>
      </div>
      <div class="file">
        <div class="file-head">
          <code>assets/{{ namespace }}/lang/en_us.json</code>
          <button @click="copy(langJson, 'lang')">{{ copied === 'lang' ? 'Copied' : 'Copy' }}</button>
        </div>
        <pre><code>{{ langJson }}</code></pre>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ap-designer {
  display: grid;
  gap: 20px;
  margin: 16px 0;
  padding: 16px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  background: var(--vp-c-bg-soft);
}

.form {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px 16px;
  font-size: 14px;
}

.numbers,
.form label.check {
  grid-column: 1 / -1;
}

.form label {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form label > span:first-child {
  font-size: 12px;
  font-weight: 600;
  color: var(--vp-c-text-2);
}

.form input:not([type='checkbox']):not([type='color']),
.form select {
  width: 100%;
  padding: 5px 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg);
  font-size: 14px;
}

.form input:focus,
.form select:focus {
  border-color: var(--vp-c-brand-1);
  outline: none;
}

.colour {
  display: flex;
  gap: 10px;
  align-items: center;
}

.colour input {
  width: 44px;
  height: 30px;
  padding: 0;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: none;
  cursor: pointer;
}

.numbers {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.form label.check {
  flex-direction: row;
  align-items: center;
  gap: 8px;
}

.out {
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-width: 0;
}

.preview {
  display: flex;
  gap: 14px;
  align-items: center;
}

.slot {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72px;
  height: 72px;
  flex: none;
  background: var(--ap-slot-bg);
  border: 3px solid;
  border-color: var(--ap-slot-dark) var(--ap-slot-light) var(--ap-slot-light) var(--ap-slot-dark);
}

.slot canvas {
  width: 64px;
  height: 64px;
}

.name {
  font-weight: 600;
  margin-bottom: 4px;
}

.file {
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  overflow: hidden;
  background: var(--vp-code-block-bg);
}

.file-head {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
  padding: 6px 10px;
  border-bottom: 1px solid var(--vp-c-divider);
  font-size: 12px;
}

.file-head code {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  background: none !important;
  padding: 0 !important;
}

.file-head button {
  flex: none;
  padding: 2px 10px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  font-size: 12px;
  color: var(--vp-c-text-2);
}

.file-head button:hover {
  color: var(--vp-c-brand-1);
  border-color: var(--vp-c-brand-1);
}

.file pre {
  margin: 0;
  padding: 10px 12px;
  overflow-x: auto;
  font-size: 13px;
  line-height: 1.5;
}

.file pre code {
  background: none !important;
  padding: 0 !important;
  color: var(--vp-c-text-1);
}

@media (max-width: 560px) {
  .form {
    grid-template-columns: minmax(0, 1fr);
  }
}
</style>
