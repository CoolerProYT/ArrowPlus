<script setup lang="ts">
import { computed } from 'vue'
import { arrowId, useArrowPlus } from '../arrowplus'
import CraftingGrid from './CraftingGrid.vue'

const props = withDefaults(
  defineProps<{ type?: 'arrow' | 'stick' | 'feather' | 'tipped'; id?: string; stick?: string; feather?: string; fletching?: boolean }>(),
  { type: 'arrow', id: '', stick: 'stick', feather: 'feather', fletching: false },
)

const { arrow, part } = useArrowPlus()

interface Card {
  cells: (string | null)[]
  result: string
  count: number
  station: string
}

const cards = computed<Card[]>(() => {
  if (props.type === 'tipped') {
    const a = 'arrowplus:diamond_arrow'
    return [{ cells: [a, a, a, a, 'arrowplus:lingering_potion_of_poison', a, a, a, a], result: 'arrowplus:diamond_arrow_tipped', count: 8, station: 'Crafting' }]
  }
  if (props.type === 'stick') {
    const stick = part('stick', props.id)
    if (!stick || stick.standalone) return []
    const m = stick.material[0]
    return [{ cells: [null, m, null, null, m, null, null, null, null], result: stick.item, count: stick.outputAmount, station: 'Crafting' }]
  }
  if (props.type === 'feather') {
    const feather = part('feather', props.id)
    if (!feather || feather.standalone) return []
    const m = feather.material[0]
    return [{ cells: [null, m, null, m, 'minecraft:feather', m, null, m, null], result: feather.item, count: feather.outputAmount, station: 'Crafting' }]
  }
  const a = arrow(props.id)
  const stick = part('stick', props.stick)
  const feather = part('feather', props.feather)
  if (!a || !stick || !feather) return []
  const vanillaParts = stick.id === 'arrowplus:stick' && feather.id === 'arrowplus:feather'
  const result = a.id === 'arrowplus:flint' && vanillaParts ? 'minecraft:arrow' : arrowId(a.id, stick.id, feather.id)
  const cards: Card[] = [
    { cells: [null, a.material, null, null, stick.item, null, null, feather.item, null], result, count: a.outputAmount, station: 'Crafting' },
  ]
  if (props.fletching && vanillaParts && a.fletchingAmount) {
    cards.push({ cells: [a.material, 'minecraft:stick', 'minecraft:feather'], result, count: a.fletchingAmount, station: 'Fletching' })
  }
  return cards
})
</script>

<template>
  <div v-if="cards.length" class="ap-recipes">
    <CraftingGrid v-for="(card, i) in cards" :key="i" v-bind="card" />
  </div>
  <p v-else class="ap-muted">Recipe {{ type }} {{ id }} not found.</p>
</template>

<style scoped>
.ap-recipes {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin: 8px 0;
}
</style>
