<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { itemIcon, itemName, tagItems } from '../arrowplus'

const props = withDefaults(defineProps<{ id?: string | null; count?: number; label?: boolean; large?: boolean }>(), {
  id: null,
  count: 1,
  label: false,
  large: false,
})

// A tag slot cycles through the tag's items once a second, like JEI and REI.
const tick = ref(0)
let timer: ReturnType<typeof setInterval> | undefined
onMounted(() => (timer = setInterval(() => tick.value++, 1000)))
onBeforeUnmount(() => clearInterval(timer))

const shown = computed(() => {
  if (!props.id) return null
  const items = tagItems(props.id)
  return items[tick.value % items.length]
})
const name = computed(() => (props.id ? itemName(props.id) : ''))
const src = computed(() => (shown.value ? itemIcon(shown.value) : null))

// Falls back to initials when an item has no icon or the hosted icon fails to load.
const failed = ref(false)
watch(src, () => (failed.value = false))
const initials = computed(() =>
  name.value
    .split(' ')
    .filter((word) => /^[A-Z]/.test(word))
    .slice(0, 2)
    .map((word) => word[0])
    .join(''),
)
</script>

<template>
  <span class="ap-item" :class="{ 'with-label': label }">
    <span class="ap-slot" :class="{ large }" :title="name" :aria-label="name" role="img">
      <img v-if="src && !failed" class="pixelated" :src="src" alt="" loading="lazy" @error="failed = true" />
      <span v-else-if="id" class="ap-initials">{{ initials }}</span>
      <span v-if="count > 1" class="ap-count">{{ count }}</span>
    </span>
    <span v-if="label && id" class="ap-label">{{ name }}</span>
  </span>
</template>

<style scoped>
.ap-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  vertical-align: middle;
}

.ap-slot {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  flex: none;
  background: var(--ap-slot-bg);
  border: 2px solid;
  border-color: var(--ap-slot-dark) var(--ap-slot-light) var(--ap-slot-light) var(--ap-slot-dark);
}

.ap-slot img {
  width: 32px;
  height: 32px;
}

.ap-slot.large {
  width: 52px;
  height: 52px;
}

.ap-slot.large img {
  width: 48px;
  height: 48px;
}

.ap-initials {
  font: 600 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.ap-count {
  position: absolute;
  right: 1px;
  bottom: -1px;
  font: 700 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.large .ap-count {
  font-size: 14px;
}

.ap-label {
  font-weight: 500;
}
</style>
