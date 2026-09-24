<script setup>
import { data } from './.vitepress/theme/arrowplus'
</script>

# Config

Arrow+ has a TOML config for server owners and modpack makers. Changes are picked up while the game is running.

```
config/arrowplus-common.toml
```

## Options

### Restrictions

| Setting | Type | Default | Description |
| --- | --- | --- | --- |
| `restrictions` | list of arrow names | `[]` | Arrows to disable. Disabled arrows cannot be crafted or used. |
| `infinityBlacklist` | list of arrow names | `[]` | Arrows that are **not** kept by the Infinity enchantment. |

### Recipe viewers

| Setting | Type | Default | Description |
| --- | --- | --- | --- |
| `hideTippedArrow` | boolean | `false` | Hides tipped Arrow+ arrows from JEI and REI. |

::: info
`infinityBlacklist` and `hideTippedArrow` only exist in **26.1+** and **1.21.1-NeoForge**. Older versions only have `restrictions`, see the [legacy config](/legacy/config).
:::

## Default file

```toml
[Restrictions]
	# A list of arrow to be disabled. Example: ['diamond', 'iron']
	restrictions = []
	# A list of arrow that won't be affected by infinity enchantment. Example: ['diamond', 'iron']
	infinityBlacklist = []

["Recipe Viewers"]
	# Hide tipped arrow+ arrow from recipe viewers
	hideTippedArrow = false
```

## Arrow names

The config uses the arrow's datapack name, the part after `arrowplus:`:

<div class="ap-names">
  <span v-for="a in data.arrows" :key="a.id"><ItemSlot :id="a.item" /> <code>{{ a.id.split(':')[1] }}</code></span>
</div>

## Examples

Disable the Netherite Arrow:

```toml
[Restrictions]
	restrictions = ["netherite"]
```

Stop Infinity from keeping Obsidian and Netherite Arrows:

```toml
[Restrictions]
	infinityBlacklist = ["obsidian", "netherite"]
```

Hide tipped arrows from JEI and REI:

```toml
["Recipe Viewers"]
	hideTippedArrow = true
```

<style scoped>
.ap-names {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 6px 16px;
  margin: 16px 0;
}

.ap-names span {
  display: flex;
  gap: 8px;
  align-items: center;
}
</style>
