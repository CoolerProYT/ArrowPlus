<script setup>
import { current } from './.vitepress/theme/arrowplus'

const { data } = current
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
| `restrictions` | list of head names | `[]` | Heads to disable. Disabled heads cannot be crafted into arrows. |
| `infinityBlacklist` | list of head names | `[]` | Heads whose arrows are **not** kept by the Infinity enchantment. |

### Recipe viewers

| Setting | Type | Default | Description |
| --- | --- | --- | --- |
| `hideTippedArrow` | boolean | `false` | Hides tipped Arrow+ arrows from JEI. |

::: info
These options apply to heads only. Sticks and feathers can't be disabled from the config, remove them with a datapack instead.

`infinityBlacklist` and `hideTippedArrow` exist since **26.1** and in **1.21.1-NeoForge**. Older versions only have `restrictions`, see the [legacy config](/legacy/config).
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

## Head names

The config uses the head's datapack name, the part after `arrowplus:`:

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

Hide tipped arrows from JEI:

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
