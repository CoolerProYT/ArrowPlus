<script setup lang="ts">
import { computed } from 'vue'
import { percent, stat, useArrowPlus } from '../arrowplus'
import ItemSlot from './ItemSlot.vue'

const props = withDefaults(defineProps<{ kind?: 'stick' | 'feather' }>(), { kind: 'stick' })

const { data } = useArrowPlus()

const rows = computed(() =>
  [...(props.kind === 'stick' ? data.sticks : data.feathers)].sort((a, b) => Number(b.standalone) - Number(a.standalone) || a.id.localeCompare(b.id)),
)
</script>

<template>
  <table class="ap-parts">
    <thead>
      <tr>
        <th>{{ kind === 'stick' ? 'Stick' : 'Feather' }}</th>
        <th>From</th>
        <template v-if="kind === 'stick'">
          <th title="Downward pull per tick. Vanilla: 0.05">Gravity</th>
          <th title="Launch speed multiplier. Vanilla: ×1">Velocity</th>
        </template>
        <template v-else>
          <th title="Multiplier on shaft gravity. Vanilla: ×1">Drop modifier</th>
          <th title="Speed lost per tick in air. Vanilla: 1%">Air loss / tick</th>
          <th title="Speed lost per tick in water. Vanilla: 40%">Water loss / tick</th>
          <th title="Spread multiplier. Below ×1 is tighter, above ×1 is wider">Spread</th>
        </template>
        <th>Id</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="p in rows" :key="p.id">
        <td><ItemSlot :id="p.item" label /></td>
        <td>
          <span v-if="p.standalone" class="ap-muted">Used directly</span>
          <span v-else class="from"><ItemSlot v-for="m in p.material" :id="m" :key="m" /> <span class="mono">×{{ p.outputAmount }}</span></span>
        </td>
        <template v-if="kind === 'stick'">
          <td class="mono">{{ stat(p.gravity) }}</td>
          <td class="mono">×{{ stat(p.velocity) }}</td>
        </template>
        <template v-else>
          <td class="mono">×{{ stat(p.gravityMultiplier) }}</td>
          <td class="mono">{{ percent(1 - p.airDrag) }}</td>
          <td class="mono">{{ percent(1 - p.waterDrag) }}</td>
          <td class="mono">×{{ stat(p.accuracy) }}</td>
        </template>
        <td><code>{{ p.id }}</code></td>
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

.from {
  display: inline-flex;
  gap: 6px;
  align-items: center;
}
</style>
