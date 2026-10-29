import DefaultTheme from 'vitepress/theme'
import type { Theme } from 'vitepress'
import ArrowBuilder from './components/ArrowBuilder.vue'
import ArrowDesigner from './components/ArrowDesigner.vue'
import ArrowTable from './components/ArrowTable.vue'
import ItemSlot from './components/ItemSlot.vue'
import PartTable from './components/PartTable.vue'
import RecipeCard from './components/RecipeCard.vue'
import VersionSwitcher from './components/VersionSwitcher.vue'
import ArrowDesignerV261 from './components/v26-1/ArrowDesigner.vue'
import ArrowTableV261 from './components/v26-1/ArrowTable.vue'
import PartTableV261 from './components/v26-1/PartTable.vue'
import RecipeCardV261 from './components/v26-1/RecipeCard.vue'
import './style.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('ArrowBuilder', ArrowBuilder)
    app.component('ArrowDesigner', ArrowDesigner)
    app.component('ArrowTable', ArrowTable)
    app.component('ItemSlot', ItemSlot)
    app.component('PartTable', PartTable)
    app.component('RecipeCard', RecipeCard)
    app.component('VersionSwitcher', VersionSwitcher)

    app.component('ArrowDesignerV261', ArrowDesignerV261)
    app.component('ArrowTableV261', ArrowTableV261)
    app.component('PartTableV261', PartTableV261)
    app.component('RecipeCardV261', RecipeCardV261)
  },
} satisfies Theme
