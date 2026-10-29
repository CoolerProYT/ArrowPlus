<script setup lang="ts">
import { computed, ref } from 'vue'
import { arrowId, number, percent, stat, useArrowPlus } from '../arrowplus'
import ItemSlot from './ItemSlot.vue'
import RecipeCard from './RecipeCard.vue'

const { data, itemName, part } = useArrowPlus()

const head = ref('arrowplus:diamond')
const stickId = ref('arrowplus:bamboo')
const featherId = ref('arrowplus:phantom_membrane')

const byName = <T extends { item: string }>(list: T[]) => [...list].sort((a, b) => itemName(a.item).localeCompare(itemName(b.item)))
const heads = computed(() => byName(data.arrows))
const sticks = computed(() => byName(data.sticks))
const feathers = computed(() => byName(data.feathers))

const arrow = computed(() => data.arrows.find((a) => a.id === head.value)!)
const stick = computed(() => part('stick', stickId.value)!)
const feather = computed(() => part('feather', featherId.value)!)

const BOW_SPEED = 3
const speed = computed(() => BOW_SPEED * stick.value.velocity)
const hit = computed(() => Math.ceil(speed.value * arrow.value.baseDamage))
</script>

<template>
  <div class="ap-builder">
    <div class="pickers">
      <label>
        <span>Head</span>
        <select v-model="head">
          <option v-for="a in heads" :key="a.id" :value="a.id">{{ itemName(a.item) }}</option>
        </select>
      </label>
      <label>
        <span>Stick</span>
        <select v-model="stickId">
          <option v-for="s in sticks" :key="s.id" :value="s.id">{{ itemName(s.item) }}</option>
        </select>
      </label>
      <label>
        <span>Feather</span>
        <select v-model="featherId">
          <option v-for="f in feathers" :key="f.id" :value="f.id">{{ itemName(f.item) }}</option>
        </select>
      </label>
    </div>

    <div class="result">
      <ItemSlot :id="arrowId(arrow.id, stick.id, feather.id)" large />
      <ul class="stats">
        <li class="heading">{{ itemName(arrow.item) }}</li>
        <li><span>Base damage</span> {{ number(arrow.baseDamage) }}</li>
        <li><span>Flame</span> {{ arrow.flame ? 'Yes' : 'No' }}</li>
        <li><span>Arrow drop</span> ×{{ stat(stick.gravity * feather.gravityMultiplier / 0.05) }}</li>
        <li><span>Launch speed</span> ×{{ stat(stick.velocity) }}</li>
        <li><span>Air loss / tick</span> {{ percent(1 - feather.airDrag) }}</li>
        <li><span>Water loss / tick</span> {{ percent(1 - feather.waterDrag) }}</li>
        <li><span>Spread</span> ×{{ stat(feather.accuracy) }}</li>
        <li class="hit" title="Before crits and Power. The vanilla arrow hits for 6.">
          <span>Full bow hit</span> {{ hit }} damage <small>at point blank</small>
        </li>
      </ul>
    </div>

    <RecipeCard :id="arrow.id" :stick="stick.id" :feather="feather.id" />
  </div>
</template>

<style scoped>
.ap-builder {
  margin: 16px 0;
  padding: 16px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  background: var(--vp-c-bg-soft);
}

.pickers {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 10px 16px;
}

.pickers label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

.pickers select {
  padding: 6px 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg);
  color: var(--vp-c-text-1);
  font-size: 14px;
}

.result {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-start;
  margin: 16px 0 4px;
}

.stats {
  list-style: none;
  padding: 0 !important;
  margin: 0 !important;
  font-size: 14px;
}

.stats li {
  margin: 2px 0 !important;
}

.stats span {
  display: inline-block;
  width: 110px;
  color: var(--vp-c-text-2);
}

.stats .heading {
  font-weight: 600;
}

.stats .hit {
  margin-top: 6px !important;
  font-weight: 600;
}

.stats small {
  font-weight: 400;
  color: var(--vp-c-text-3);
}
</style>
