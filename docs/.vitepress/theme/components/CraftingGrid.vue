<script setup lang="ts">
import ItemSlot from './ItemSlot.vue'

/** Nine cells left to right, top to bottom (null for empty), or three for a Fletching Table column. */
withDefaults(defineProps<{ cells: (string | null)[]; result: string; count?: number; station?: string }>(), {
  count: 1,
  station: 'Crafting',
})
</script>

<template>
  <div class="ap-recipe">
    <div class="grid" :class="{ column: cells.length === 3 }">
      <ItemSlot v-for="(cell, i) in cells" :id="cell" :key="i" />
    </div>
    <span class="arrow">
      <span class="station">{{ station }}</span>
      ➜
    </span>
    <ItemSlot :id="result" :count="count" label large />
  </div>
</template>

<style scoped>
.ap-recipe {
  display: inline-flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
  margin: 8px 12px 8px 0;
  padding: 12px 16px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 10px;
  background: var(--vp-c-bg-soft);
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 36px);
}

.grid.column {
  grid-template-columns: 36px;
}

.arrow {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 22px;
  line-height: 1;
  color: var(--vp-c-text-2);
}

.station {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}
</style>
