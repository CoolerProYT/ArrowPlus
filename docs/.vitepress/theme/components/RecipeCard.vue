<script setup lang="ts">
import { computed } from 'vue'
import { arrow, part } from '../arrowplus'
import CraftingGrid from './CraftingGrid.vue'

/**
 * `type` picks the recipe: an arrow (`id` = arrow entry), a custom stick or feather (`id` = stick/feather entry),
 * or the tipped arrow recipe. `fletching` adds the Fletching Table version of an arrow recipe.
 */
const props = withDefaults(
  defineProps<{ type?: 'arrow' | 'stick' | 'feather' | 'tipped'; id?: string; fletching?: boolean }>(),
  { type: 'arrow', id: '', fletching: false },
)

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
    if (!stick) return []
    const m = stick.material[0]
    return [{ cells: [null, m, null, null, m, null, null, null, null], result: stick.item, count: stick.outputAmount, station: 'Crafting' }]
  }
  if (props.type === 'feather') {
    const feather = part('feather', props.id)
    if (!feather) return []
    const m = feather.material[0]
    return [{ cells: [null, m, null, m, 'minecraft:feather', m, null, m, null], result: feather.item, count: feather.outputAmount, station: 'Crafting' }]
  }
  const a = arrow(props.id)
  if (!a) return []
  const result: Card[] = [
    { cells: [null, a.material, null, null, a.stick, null, null, a.feather, null], result: a.item, count: a.outputAmount, station: 'Crafting' },
  ]
  if (props.fletching && a.fletchingAmount) {
    result.push({ cells: [a.material, a.stick, a.feather], result: a.item, count: a.fletchingAmount, station: 'Fletching' })
  }
  return result
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
