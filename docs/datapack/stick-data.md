# Stick data

Every stick is an entry in the `arrowplus/sticks` datapack registry. Add your own and players can use it with any [head](./arrow-data) and [feather](./feather-data).

```
data/<namespace>/arrowplus/sticks/<name>.json
```

For example, `data/mymod/arrowplus/sticks/amethyst.json` adds the stick `mymod:amethyst`.

A stick is either **standalone**, where the material item itself goes in the arrow recipe (like bamboo), or **crafted** into an `arrowplus:custom_stick` first (like the iron stick).

## Fields

| Field | Type | Required | Description |
| --- | --- | :---: | --- |
| `material` | item id, `#tag` or list of item ids | ✅ | Standalone: the items used as the stick. Crafted: what the stick is crafted from. |
| `color` | int | ✅ | Tint of the stick on the arrow (and of the custom stick item), as a **signed decimal ARGB** int. The [arrow designer](./arrow-data#arrow-designer) converts colors. |
| `translationKey` | string | ✅ | Lang key for the stick's name. Standalone sticks can use the item's own key, e.g. `block.minecraft.bamboo`. |
| `gravity` | number | | How fast the arrow drops each tick. Defaults to `0.05`, the vanilla arrow. |
| `velocity` | number | | Launch speed multiplier. Defaults to `1.0`. Also scales [damage](../arrows#damage). |
| `standalone` | boolean | | `true` to use the material item directly. Defaults to `false`. |
| `outputAmount` | int | | Crafted sticks per craft. Defaults to `4`. Not used by standalone sticks. |

Physics values must be finite: gravity accepts `0`–`1` and velocity accepts `0.01`–`4`. Output amount accepts `1`–`64`. Invalid values fail datapack loading with a field error.

## Crafting

Crafted sticks: two of the material in a column make `outputAmount` sticks.

<RecipeCard type="stick" id="emerald" />

Standalone sticks have no recipe of their own.

## Examples

### Crafted stick

```json
{
  "material": "minecraft:amethyst_shard",
  "color": -6543440,
  "translationKey": "item.mymod.amethyst_stick",
  "gravity": 0.045,
  "velocity": 1.1,
  "outputAmount": 4
}
```

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.amethyst_stick": "Amethyst Stick"
}
```

### Standalone stick

The built-in bamboo stick:

```json
{
  "material": "minecraft:bamboo",
  "color": -8412366,
  "translationKey": "block.minecraft.bamboo",
  "gravity": 0.045,
  "velocity": 0.9,
  "standalone": true
}
```

::: tip Material conflicts
An item should only be the material of one standalone stick. If two match, the first one wins.
:::

## Built-in entries

<PartTable kind="stick" />
