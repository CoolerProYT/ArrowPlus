import DefaultTheme from 'vitepress/theme'
import type { Theme } from 'vitepress'
import ArrowDesigner from './components/ArrowDesigner.vue'
import ArrowTable from './components/ArrowTable.vue'
import ItemSlot from './components/ItemSlot.vue'
import PartTable from './components/PartTable.vue'
import RecipeCard from './components/RecipeCard.vue'
import VersionSwitcher from './components/VersionSwitcher.vue'
import './style.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('ArrowDesigner', ArrowDesigner)
    app.component('ArrowTable', ArrowTable)
    app.component('ItemSlot', ItemSlot)
    app.component('PartTable', PartTable)
    app.component('RecipeCard', RecipeCard)
    app.component('VersionSwitcher', VersionSwitcher)
  },
} satisfies Theme
