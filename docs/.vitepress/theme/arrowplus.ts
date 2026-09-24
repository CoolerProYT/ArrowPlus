// @ts-ignore
import raw from '../data/data.json'

export interface Arrow {
  id: string
  /** The arrow's own item id for icons and names, e.g. arrowplus:copper_arrow. */
  item: string
  /** Item id, or `#tag`. */
  material: string
  stick: string
  feather: string
  outputAmount: number
  /** Arrows per craft at a Fletching Table (Fletching Recipe mod), when the mod ships that recipe. */
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
  /** Item ids or a single `#tag`. */
  material: string[]
  color: number
  outputAmount: number
}

export const data = raw as unknown as {
  names: Record<string, string>
  textures: Record<string, string>
  arrows: Arrow[]
  sticks: Part[]
  feathers: Part[]
}

/** Items a tag slot cycles through, like recipe viewers do. */
const TAG_ITEMS: Record<string, string[]> = {
  '#minecraft:planks': ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'mangrove', 'cherry', 'bamboo', 'crimson', 'warped'].map(
    (wood) => `minecraft:${wood}_planks`,
  ),
  '#minecraft:stone_crafting_materials': ['minecraft:cobblestone', 'minecraft:blackstone', 'minecraft:cobbled_deepslate'],
}

const TAG_NAMES: Record<string, string> = {
  '#minecraft:planks': 'Any Planks',
  '#minecraft:stone_crafting_materials': 'Cobblestone, Blackstone or Cobbled Deepslate',
}

/** Names for icons that are not datapack entries. */
const EXTRA_NAMES: Record<string, string> = {
  'minecraft:quartz': 'Nether Quartz',
  'arrowplus:diamond_arrow_tipped': 'Diamond Arrow of Poison',
  'arrowplus:lingering_potion_of_poison': 'Lingering Potion of Poison',
  'arrowplus:custom_stick': 'Custom Stick',
  'arrowplus:custom_feather': 'Custom Feather',
}

const HOSTED = 'https://storage.googleapis.com/coolerpromc/textures'

export function tagItems(id: string): string[] {
  return TAG_ITEMS[id] ?? [id]
}

/** Mod items use their in-game name; vanilla ids are turned into readable names. */
export function itemName(id: string): string {
  if (data.names[id]) return data.names[id]
  if (TAG_NAMES[id]) return TAG_NAMES[id]
  if (EXTRA_NAMES[id]) return EXTRA_NAMES[id]
  const path = id.replace(/^#/, '').split(':').pop() ?? id
  return path
    .split('_') // @ts-ignore
    .map((word) => (['of', 'the'].includes(word) ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join(' ')
}

/** Hosted 1024x1024 renders: vanilla under minecraft/, every Arrow+ variant under arrowplus/. */
export function itemIcon(id: string): string | null {
  if (data.textures[id]) return data.textures[id]
  if (id.startsWith('#')) return null
  // @ts-ignore
  const [namespace, path] = id.includes(':') ? id.split(':') : ['minecraft', id]
  return `${HOSTED}/${namespace}/${path}.png`
}

export function arrow(id: string): Arrow | undefined {
  const full = id.includes(':') ? id : `arrowplus:${id}`
  return data.arrows.find((a) => a.id === full || a.item === full)
}

export function part(kind: 'stick' | 'feather', id: string): Part | undefined {
  const full = id.includes(':') ? id : `arrowplus:${id}`
  const list = kind === 'stick' ? data.sticks : data.feathers
  return list.find((p) => p.id === full || p.item === full)
}

/** Datapack colors are signed ARGB ints. */
export function hex(color: number): string {
  return `#${((color >>> 0) & 0xffffff).toString(16).padStart(6, '0').toUpperCase()}`
}

export function number(value: number): string {
  return Number.isInteger(value) ? value.toFixed(1) : `${value}`
}

export const maxDamage = Math.max(...data.arrows.map((a) => a.baseDamage), 2)
