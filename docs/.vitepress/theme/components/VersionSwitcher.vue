<script setup lang="ts">
import { computed, ref } from 'vue'
import { useData, useRouter, withBase } from 'vitepress'

const router = useRouter()
const { page } = useData()
const isOpen = ref(false)

// Two "systems", each covering several Minecraft versions. `prefix` is the page path prefix.
const versions = [
  { label: '26.1+ / 1.21.1-NeoForge', tag: 'New', prefix: '' },
  { label: '1.21.11 and older', tag: 'Legacy', prefix: 'legacy/' },
]

// Pages that exist under a different name in the other system.
const RENAMED: Record<string, string> = {
  'custom-sticks': 'material-sticks',
  'custom-feathers': 'material-sticks',
  'datapack/stick-data': 'material-sticks',
  'datapack/feather-data': 'material-sticks',
  'material-sticks': 'custom-sticks',
}
const LEGACY_PAGES = ['getting-started', 'arrows', 'material-sticks', 'tipped-arrows', 'config', 'datapack/arrow-data']
const NEW_PAGES = ['getting-started', 'arrows', 'custom-sticks', 'custom-feathers', 'tipped-arrows', 'config', 'datapack/arrow-data', 'datapack/stick-data', 'datapack/feather-data']

const current = computed(() => (page.value.relativePath.startsWith('legacy/') ? versions[1] : versions[0]))

function switchVersion(v: (typeof versions)[number]) {
  isOpen.value = false
  if (v === current.value) return
  const name = page.value.relativePath.replace(/^legacy\//, '').replace(/(index)?\.md$/, '')
  const target = RENAMED[name] ?? name
  const pages = v.prefix ? LEGACY_PAGES : NEW_PAGES
  router.go(withBase(`/${v.prefix}${pages.includes(target) ? target : 'getting-started'}`))
}
</script>

<template>
  <div class="version-switcher" @mouseenter="isOpen = true" @mouseleave="isOpen = false">
    <button class="button" type="button" :aria-expanded="isOpen" @click="isOpen = !isOpen">
      <span class="tag" :class="current.tag.toLowerCase()">{{ current.tag }}</span>
      <span class="text">{{ current.label }}</span>
      <svg class="icon" width="14" height="14" viewBox="0 0 24 24" fill="none">
        <path d="M6 9l6 6 6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </button>

    <div v-if="isOpen" class="menu">
      <button v-for="v in versions" :key="v.label" class="item" :class="{ active: v === current }" @click="switchVersion(v)">
        <span class="tag" :class="v.tag.toLowerCase()">{{ v.tag }}</span>
        {{ v.label }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.version-switcher {
  position: relative;
  display: flex;
  align-items: center;
  margin-left: 8px;
}

.button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0 12px;
  line-height: var(--vp-nav-height);
  font-size: 14px;
  font-weight: 500;
  color: var(--vp-c-text-1);
  transition: color 0.25s;
}

.button:hover {
  color: var(--vp-c-brand-1);
}

.icon {
  transition: transform 0.25s;
}

.button[aria-expanded='true'] .icon {
  transform: rotate(180deg);
}

.tag {
  padding: 0 7px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 700;
  line-height: 18px;
  text-transform: uppercase;
  letter-spacing: 0.03em;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
}

.tag.legacy {
  background: var(--vp-c-default-soft);
  color: var(--vp-c-text-2);
}

.menu {
  position: absolute;
  top: calc(100% - 12px);
  right: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 250px;
  padding: 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  background: var(--vp-c-bg-elv);
  box-shadow: var(--vp-shadow-3);
  z-index: 100;
}

.item {
  display: flex;
  gap: 8px;
  align-items: center;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 14px;
  text-align: left;
  white-space: nowrap;
  color: var(--vp-c-text-1);
  transition: background-color 0.25s, color 0.25s;
}

.item:hover {
  background: var(--vp-c-default-soft);
  color: var(--vp-c-brand-1);
}

.item.active {
  font-weight: 600;
  color: var(--vp-c-brand-1);
}

@media (max-width: 1199px) {
  .text {
    display: none;
  }
}
</style>
