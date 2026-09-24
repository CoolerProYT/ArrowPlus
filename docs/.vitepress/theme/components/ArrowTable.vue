<script setup lang="ts">
import { computed, ref } from 'vue'
import { data, itemName, maxDamage, number, type Arrow } from '../arrowplus'
import ItemSlot from './ItemSlot.vue'
import RecipeCard from './RecipeCard.vue'

type Sort = 'damage' | 'name' | 'output'
const SORTS: { key: Sort; label: string }[] = [
  { key: 'damage', label: 'Damage' },
  { key: 'output', label: 'Output' },
  { key: 'name', label: 'Name' },
]

const sort = ref<Sort>('damage')
const specialOnly = ref(false)
const open = ref<string | null>(null)

const special = (a: Arrow) => a.stick !== 'minecraft:stick' || a.feather !== 'minecraft:feather'

const arrows = computed(() =>
  data.arrows
    .filter((a) => !specialOnly.value || special(a))
    .sort((a, b) => {
      if (sort.value === 'name') return itemName(a.item).localeCompare(itemName(b.item))
      if (sort.value === 'output') return b.outputAmount - a.outputAmount || b.baseDamage - a.baseDamage
      return b.baseDamage - a.baseDamage || itemName(a.item).localeCompare(itemName(b.item))
    }),
)

function toggle(id: string) {
  open.value = open.value === id ? null : id
}
</script>

<template>
  <div class="ap-arrows">
    <div class="controls">
      <div class="chips" role="group" aria-label="Sort by">
        <span class="hint">Sort</span>
        <button v-for="s in SORTS" :key="s.key" :class="{ active: sort === s.key }" :aria-pressed="sort === s.key" @click="sort = s.key">
          {{ s.label }}
        </button>
      </div>
      <label class="filter"><input v-model="specialOnly" type="checkbox" /> Only arrows with special sticks or feathers</label>
    </div>

    <div class="table">
      <div class="row head">
        <span>Arrow</span>
        <span>Recipe</span>
        <span class="num">Makes</span>
        <span>Base damage</span>
      </div>
      <template v-for="a in arrows" :key="a.id">
        <button class="row" :aria-expanded="open === a.id" @click="toggle(a.id)">
          <span class="arrow-cell"><ItemSlot :id="a.item" label /></span>
          <span class="parts">
            <ItemSlot :id="a.material" />
            <ItemSlot :id="a.stick" :class="{ custom: a.stick !== 'minecraft:stick' }" />
            <ItemSlot :id="a.feather" :class="{ custom: a.feather !== 'minecraft:feather' }" />
          </span>
          <span class="num makes">
            ×{{ a.outputAmount }}
            <small v-if="a.fletchingAmount" title="At a Fletching Table">×{{ a.fletchingAmount }} fletching</small>
          </span>
          <span class="damage">
            <span class="bar" aria-hidden="true"><span :style="{ width: `${(a.baseDamage / maxDamage) * 100}%` }" /></span>
            <span class="value">{{ number(a.baseDamage) }}</span>
          </span>
        </button>
        <div v-if="open === a.id" class="detail">
          <RecipeCard :id="a.id" fletching />
          <ul class="facts">
            <li><span>Flame</span> {{ a.flame ? 'Yes' : 'No' }}</li>
            <li><span>Gravity</span> {{ a.gravity }}</li>
            <li><span>Datapack id</span> <code>{{ a.id }}</code></li>
          </ul>
        </div>
      </template>
    </div>
    <p class="legend">Click an arrow for its recipes. Vanilla Flint Arrow: {{ number(2) }} damage.</p>
  </div>
</template>

<style scoped>
.ap-arrows {
  margin: 16px 0;
}

.controls {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 10px 18px;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
}

.chips {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
}

.hint {
  color: var(--vp-c-text-2);
  margin-right: 2px;
}

.chips button {
  padding: 3px 12px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 999px;
  color: var(--vp-c-text-2);
}

.chips button.active {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  font-weight: 600;
}

.filter {
  display: flex;
  gap: 6px;
  align-items: center;
  color: var(--vp-c-text-2);
}

.table {
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  overflow: hidden;
}

.row {
  display: grid;
  grid-template-columns: minmax(170px, 1.4fr) 132px 90px minmax(120px, 1fr);
  gap: 12px;
  align-items: center;
  width: 100%;
  padding: 8px 14px;
  text-align: left;
  border-top: 1px solid var(--vp-c-divider);
  font-size: 14px;
  transition: background-color 0.2s;
}

button.row:hover,
button.row[aria-expanded='true'] {
  background: var(--vp-c-bg-soft);
}

.row.head {
  border-top: none;
  background: var(--vp-c-bg-soft);
  font-size: 12px;
  font-weight: 600;
  color: var(--vp-c-text-2);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.parts {
  display: flex;
}

.parts .custom :deep(.ap-slot) {
  box-shadow: 0 0 0 2px var(--vp-c-brand-1);
  z-index: 1;
}

.num {
  text-align: right;
}

.makes {
  display: flex;
  flex-direction: column;
  font-family: var(--vp-font-family-mono);
  font-weight: 600;
}

.makes small {
  font-weight: 400;
  font-size: 11px;
  color: var(--vp-c-text-3);
}

.damage {
  display: flex;
  gap: 10px;
  align-items: center;
}

.bar {
  flex: 1;
  height: 8px;
  border-radius: 4px;
  background: var(--vp-c-divider);
  overflow: hidden;
}

.bar span {
  display: block;
  height: 100%;
  border-radius: 4px;
  background: linear-gradient(90deg, var(--vp-c-brand-3), var(--vp-c-brand-1));
  transition: width 0.25s;
}

.value {
  width: 2.2em;
  font-family: var(--vp-font-family-mono);
  font-weight: 600;
  text-align: right;
}

.detail {
  display: flex;
  flex-wrap: wrap;
  gap: 4px 24px;
  align-items: center;
  padding: 4px 14px 12px;
  background: var(--vp-c-bg-soft);
}

.facts {
  list-style: none;
  padding: 0 !important;
  margin: 0 !important;
  font-size: 14px;
}

.facts li {
  margin: 2px 0 !important;
}

.facts span {
  display: inline-block;
  width: 90px;
  color: var(--vp-c-text-2);
}

.legend {
  margin: 8px 0 0;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

@media (max-width: 640px) {
  .row {
    grid-template-columns: 1fr auto;
  }

  .row.head,
  .parts {
    display: none;
  }

  .damage {
    grid-column: 1 / -1;
  }
}
</style>
