import { useData, withBase } from 'vitepress'
// @ts-ignore
import currentRaw from '../data/data.json'
// @ts-ignore
import v261Raw from '../frozen/data-26.1.json'

export interface Arrow {
  id: string

  item: string

  material: string
  stick: string
  feather: string
  outputAmount: number

  fletchingAmount: number | null
  baseDamage: number
  flame: boolean
  gravity: number
  effects: Record<string, number>
  color: number
}

export interface Part {
  id: string
  item: string

  material: string[]
  color: number
  outputAmount: number
  standalone: boolean
  gravity: number
  velocity: number
  airDrag: number
  waterDrag: number
  gravityMultiplier: number
  accuracy: number
}

export type Icon = string | { layers: [string, number][] }

export interface Dataset {
  names: Record<string, string>
  textures: Record<string, Icon>
  arrows: Arrow[]
  sticks: Part[]
  feathers: Part[]
}

const TAG_ITEMS: Record<string, string[]> = {
  '#minecraft:planks': ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'mangrove', 'cherry', 'bamboo', 'crimson', 'warped'].map(
    (wood) => `minecraft:${wood}_planks`,
  ),
  '#minecraft:stone_crafting_materials': ['minecraft:cobblestone', 'minecraft:blackstone', 'minecraft:cobbled_deepslate'],
  '#c:rods/wooden': ['minecraft:stick'],
  '#c:feathers': ['minecraft:feather'],
}

const TAG_NAMES: Record<string, string> = {
  '#minecraft:planks': 'Any Planks',
  '#minecraft:stone_crafting_materials': 'Cobblestone, Blackstone or Cobbled Deepslate',
  '#c:rods/wooden': 'Any wooden rod',
  '#c:feathers': 'Any feather',
}

const EXTRA_NAMES: Record<string, string> = {
  'minecraft:quartz': 'Nether Quartz',
  'arrowplus:diamond_arrow_tipped': 'Diamond Arrow of Poison',
  'arrowplus:lingering_potion_of_poison': 'Lingering Potion of Poison',
  'arrowplus:custom_stick': 'Custom Stick',
  'arrowplus:custom_feather': 'Custom Feather',
}

const HOSTED = 'https://storage.googleapis.com/coolerpromc/textures'

export const VANILLA_STICK = 0xff886627
export const VANILLA_FEATHER = 0xffffffff

export function tagItems(id: string): string[] {
  return TAG_ITEMS[id] ?? [id]
}

export function hex(color: number): string {
  return `#${((color >>> 0) & 0xffffff).toString(16).padStart(6, '0').toUpperCase()}`
}

export function number(value: number): string {
  return Number.isInteger(value) ? value.toFixed(1) : `${value}`
}

export function stat(value: number): string {
  return `${Math.round(value * 1000) / 1000}`
}

export function percent(fraction: number): string {
  return `${stat(fraction * 100)}%`
}

export function signedPercent(fraction: number): string {
  return `${fraction > 0 ? '+' : ''}${percent(fraction)}`
}

export function arrowId(head: string, stick: string, feather: string): string {
  return `arrow:${head}|${stick}|${feather}`
}

function full(id: string): string {
  return id.includes(':') ? id : `arrowplus:${id}`
}

export function createApi(data: Dataset) {
  function arrow(id: string): Arrow | undefined {
    const key = full(id)
    return data.arrows.find((a) => a.id === key || a.item === key)
  }

  function part(kind: 'stick' | 'feather', id: string): Part | undefined {
    const key = full(id)
    const list = kind === 'stick' ? data.sticks : data.feathers
    return list.find((p) => p.id === key || p.item === key)
  }

  function itemName(id: string): string {
    if (id.startsWith('arrow:')) {
      const head = arrow(id.slice(6).split('|')[0])
      return head ? itemName(head.item) : 'Arrow'
    }
    if (data.names[id]) return data.names[id]
    if (TAG_NAMES[id]) return TAG_NAMES[id]
    if (EXTRA_NAMES[id]) return EXTRA_NAMES[id]
    const path = id.replace(/^#/, '').split(':').pop() ?? id
    return path
      .split('_') // @ts-ignore
      .map((word) => (['of', 'the'].includes(word) ? word : word.charAt(0).toUpperCase() + word.slice(1)))
      .join(' ')
  }

  function itemIcon(id: string): Icon | null {
    if (id.startsWith('arrow:')) {
      const [head, stick, feather] = id.slice(6).split('|')
      const h = arrow(head)
      if (!h) return null
      return {
        layers: [
          ['arrow_stick', part('stick', stick)?.color ?? VANILLA_STICK],
          ['arrow_head', h.color],
          ['arrow_feather', part('feather', feather)?.color ?? VANILLA_FEATHER],
        ],
      }
    }
    if (data.textures[id]) return data.textures[id]
    if (id.startsWith('#')) return null
    // @ts-ignore
    const [namespace, path] = id.includes(':') ? id.split(':') : ['minecraft', id]
    return `${HOSTED}/${namespace}/${path}.png`
  }

  const maxDamage = Math.max(...data.arrows.map((a) => a.baseDamage), 2)

  return { data, arrow, part, itemName, itemIcon, maxDamage }
}

export type Api = ReturnType<typeof createApi>

export const current = createApi(currentRaw as unknown as Dataset)

export const v261 = createApi(v261Raw as unknown as Dataset)

export function useArrowPlus(): Api {
  const { page } = useData()
  return page.value.relativePath.startsWith('v26-1/') ? v261 : current
}

const layerImages = new Map<string, Promise<HTMLImageElement | null>>()
const rendered = new Map<string, Promise<string | null>>()

function loadLayer(name: string): Promise<HTMLImageElement | null> {
  if (!layerImages.has(name)) {
    layerImages.set(
      name,
      new Promise((resolve) => {
        const image = new Image()
        image.onload = () => resolve(image)
        image.onerror = () => resolve(null)
        image.src = withBase(`/layers/${name}.png`)
      }),
    )
  }
  return layerImages.get(name)!
}

export function renderLayers(layers: [string, number][]): Promise<string | null> {
  const key = JSON.stringify(layers)
  if (!rendered.has(key)) {
    rendered.set(
      key,
      (async () => {
        const images = await Promise.all(layers.map(([name]) => loadLayer(name)))
        const canvas = document.createElement('canvas')
        canvas.width = canvas.height = 16
        const ctx = canvas.getContext('2d')!
        const scratch = document.createElement('canvas')
        scratch.width = scratch.height = 16
        const sctx = scratch.getContext('2d')!
        let drawn = false
        layers.forEach(([, argb], i) => {
          const image = images[i]
          if (!image) return
          sctx.clearRect(0, 0, 16, 16)
          sctx.drawImage(image, 0, 0)
          const pixels = sctx.getImageData(0, 0, 16, 16)
          const [r, g, b] = [(argb >>> 16) & 255, (argb >>> 8) & 255, argb & 255]
          for (let p = 0; p < pixels.data.length; p += 4) {
            pixels.data[p] = (pixels.data[p] * r) / 255
            pixels.data[p + 1] = (pixels.data[p + 1] * g) / 255
            pixels.data[p + 2] = (pixels.data[p + 2] * b) / 255
          }
          sctx.putImageData(pixels, 0, 0)
          ctx.drawImage(scratch, 0, 0)
          drawn = true
        })
        return drawn ? canvas.toDataURL() : null
      })(),
    )
  }
  return rendered.get(key)!
}
