# Arrow data

Every arrow is an entry in the `arrowplus/arrows` datapack registry. A datapack can add new arrows, or change built-in ones by overwriting their file.

```
data/<namespace>/arrowplus/arrows/<name>.json
```

For example, `data/mymod/arrowplus/arrows/ruby.json` adds the arrow `mymod:ruby`. The mod's own arrows live in `data/arrowplus/arrowplus/arrows/`.

## Arrow designer

Pick the stats and the colors, and copy the two files. The preview uses the mod's own textures, tinted the same way the game does it.

<ArrowDesigner />

## Fields

All fields are required, except `stickData` and `featherData`, which are only needed with custom sticks and feathers.

| Field | Type | Description |
| --- | --- | --- |
| `material` | item id, `#tag` or list | The top item of the recipe. |
| `baseDamage` | number | Damage on hit before enchantments. The vanilla Flint Arrow does `2.0`. |
| `color` | int | Color of the arrow head, as a **signed decimal ARGB** int. `-12532481` is `#40C4FF`. The designer above converts for you. |
| `translationKey` | string | Lang key for the arrow's name, e.g. `item.mymod.ruby_arrow`. |
| `flame` | boolean | Whether the arrow sets the target on fire. |
| `gravity` | number | How fast the arrow drops. Built-in arrows use `0.05`; lower is flatter. |
| `effects` | object | Effects applied on hit, effect id → amplifier. `{}` for none. |
| `feather` | item id | The bottom item: `minecraft:feather`, or `arrowplus:custom_feather` with `featherData`. |
| `featherData` | feather id | Which [custom feather](./feather-data), e.g. `arrowplus:gilded`. Needed with `arrowplus:custom_feather`. |
| `stick` | item id | The middle item: `minecraft:stick`, or `arrowplus:custom_stick` with `stickData`. |
| `stickData` | stick id | Which [custom stick](./stick-data), e.g. `arrowplus:copper`. Needed with `arrowplus:custom_stick`. |
| `outputAmount` | int | Arrows per craft. The Fletching Table makes double. |

::: warning stickData and featherData
If `stick` is `arrowplus:custom_stick`, the entry **must** have `stickData`. If `feather` is `arrowplus:custom_feather`, it **must** have `featherData`. Otherwise the datapack fails to load.
:::

## Examples

### Plain arrow

<RecipeCard id="copper" />

```json
{
  "material": "minecraft:copper_ingot",
  "baseDamage": 2.2,
  "color": -2855612,
  "translationKey": "item.arrowplus.copper_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "minecraft:feather",
  "stick": "minecraft:stick",
  "outputAmount": 4
}
```

### Custom stick

<RecipeCard id="iron" />

```json
{
  "material": "minecraft:iron_ingot",
  "baseDamage": 2.5,
  "color": -5194043,
  "translationKey": "item.arrowplus.iron_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "minecraft:feather",
  "stick": "arrowplus:custom_stick",
  "stickData": "arrowplus:copper",
  "outputAmount": 4
}
```

### Custom stick and feather

<RecipeCard id="obsidian" />

```json
{
  "material": "minecraft:obsidian",
  "baseDamage": 3.8,
  "color": -13755833,
  "translationKey": "item.arrowplus.obsidian_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "arrowplus:custom_feather",
  "featherData": "arrowplus:gilded",
  "stick": "arrowplus:custom_stick",
  "stickData": "arrowplus:diamond",
  "outputAmount": 2
}
```

### Tag as material

<RecipeCard id="stone" />

```json
{
  "material": "#minecraft:stone_crafting_materials",
  "baseDamage": 1.5,
  "color": -11711671,
  "translationKey": "item.arrowplus.stone_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "minecraft:feather",
  "stick": "minecraft:stick",
  "outputAmount": 8
}
```

### Flame arrow with an effect

A new arrow that sets targets on fire and slows them:

```json
{
  "material": "minecraft:blaze_rod",
  "baseDamage": 2.5,
  "color": -1497344,
  "translationKey": "item.mymod.blaze_arrow",
  "flame": true,
  "gravity": 0.05,
  "effects": {
    "minecraft:slowness": 0
  },
  "feather": "minecraft:feather",
  "stick": "minecraft:stick",
  "outputAmount": 4
}
```

## Name

Add the `translationKey` to a resource pack's lang file:

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.blaze_arrow": "Blaze Arrow"
}
```

## Disabling built-in arrows

To turn off a built-in arrow without a datapack, list it in `restrictions` in the [config](../config).
