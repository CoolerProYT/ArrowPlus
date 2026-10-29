# Feather data

Every feather is an entry in the `arrowplus/feathers` datapack registry. Add your own and players can use it with any [head](./arrow-data) and [stick](./stick-data).

```
data/<namespace>/arrowplus/feathers/<name>.json
```

For example, `data/mymod/arrowplus/feathers/spectral.json` adds the feather `mymod:spectral`.

A feather is either **standalone**, where the material item itself goes in the arrow recipe (like phantom membrane), or **crafted** into an `arrowplus:custom_feather` first (like the Kelp Feather).

## Fields

| Field | Type | Required | Description |
| --- | --- | :---: | --- |
| `material` | item id, `#tag` or list of item ids | ✅ | Standalone: the items used as the feather. Crafted: what the feather is crafted from. |
| `color` | int | ✅ | Tint of the feather on the arrow (and of the custom feather item), as a **signed decimal ARGB** int. The [arrow designer](./arrow-data#arrow-designer) converts colors. |
| `translationKey` | string | ✅ | Lang key for the feather's name. Standalone feathers can use the item's own key, e.g. `item.minecraft.paper`. |
| `gravityMultiplier` | number | | Multiplies shaft gravity. Defaults to `1.0`; allowed range `0`–`4`. Phantom membrane uses `0.6`. |
| `airDrag` | number | | Fraction of speed **kept** each tick in air. Defaults to `0.99`, the vanilla arrow. Higher flies further. |
| `waterDrag` | number | | Fraction of speed **kept** each tick in water. Defaults to `0.6`, the vanilla arrow. |
| `accuracy` | number | | Spread multiplier. Defaults to `1.0`. Lower is more accurate: `0.5` halves the spread. |
| `standalone` | boolean | | `true` to use the material item directly. Defaults to `false`. |
| `outputAmount` | int | | Crafted feathers per craft. Defaults to `4`. Not used by standalone feathers. |

The tooltip shows drag as speed **lost** per tick, so `airDrag: 0.99` is shown as `1%`.

All physics values must be finite. Air and water drag must be between `0` and `1`, accuracy between `0` and `10`, and output amount between `1` and `64`. Invalid values fail datapack loading with a field error.

## Crafting

Crafted feathers: four of the material around a vanilla feather make `outputAmount` feathers.

<RecipeCard type="feather" id="kelp" />

Standalone feathers have no recipe of their own.

## Examples

### Crafted feather

```json
{
  "material": "minecraft:glow_ink_sac",
  "color": -16711681,
  "translationKey": "item.mymod.spectral_feather",
  "airDrag": 0.992,
  "accuracy": 0.7,
  "outputAmount": 4
}
```

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.spectral_feather": "Spectral Feather"
}
```

### Standalone feather

The built-in phantom membrane feather:

```json
{
  "material": "minecraft:phantom_membrane",
  "color": -3555104,
  "translationKey": "item.minecraft.phantom_membrane",
  "airDrag": 0.995,
  "accuracy": 0.8,
  "gravityMultiplier": 0.6,
  "standalone": true
}
```

::: tip Material conflicts
An item should only be the material of one standalone feather. If two match, the first one wins.
:::

## Built-in entries

<PartTable kind="feather" />
