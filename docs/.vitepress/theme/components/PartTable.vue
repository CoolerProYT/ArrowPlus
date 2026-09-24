<script setup lang="ts">
import { computed } from 'vue'
import { data, hex } from '../arrowplus'
import ItemSlot from './ItemSlot.vue'

/** Built-in sticks or feathers, with the arrows that use each one. */
const props = withDefaults(defineProps<{ kind?: 'stick' | 'feather' }>(), { kind: 'stick' })

const rows = computed(() =>
  (props.kind === 'stick' ? data.sticks : data.feathers).map((p) => ({
    ...p,
    usedBy: data.arrows.filter((a) => (props.kind === 'stick' ? a.stick : a.feather) === p.item),
  })),
)
</script>

<template>
  <table class="ap-parts">
    <thead>
      <tr>
        <th>{{ kind === 'stick' ? 'Stick' : 'Feather' }}</th>
        <th>Material</th>
        <th>Makes</th>
        <th>Color</th>
        <th>Used by</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="p in rows" :key="p.id">
        <td><ItemSlot :id="p.item" label /></td>
        <td><ItemSlot v-for="m in p.material" :id="m" :key="m" /></td>
        <td class="mono">×{{ p.outputAmount }}</td>
        <td class="mono"><span class="swatch" :style="{ background: hex(p.color) }" />{{ hex(p.color) }}</td>
        <td>
          <span v-if="!p.usedBy.length" class="ap-muted">—</span>
          <span class="used">
            <ItemSlot v-for="a in p.usedBy" :id="a.item" :key="a.id" />
          </span>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
.ap-parts td {
  vertical-align: middle;
}

.mono {
  font-family: var(--vp-font-family-mono);
  font-size: 13px;
  white-space: nowrap;
}

.swatch {
  display: inline-block;
  width: 12px;
  height: 12px;
  margin-right: 6px;
  border-radius: 3px;
  border: 1px solid var(--vp-c-divider);
  vertical-align: -1px;
}

.used {
  display: flex;
  flex-wrap: wrap;
}
</style>
