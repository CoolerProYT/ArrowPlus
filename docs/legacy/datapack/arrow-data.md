# Arrow Data

On legacy versions, Arrow+ uses a **datapack registry** (`arrowplus/arrows/`) to define arrow types. Arrows are datapack-driven, but sticks and feathers reference **specific registered item IDs** — not datapack entries.

## File Location

```
data/<namespace>/arrowplus/arrows/<name>.json
```

**Example:**
```
data/mymod/arrowplus/arrows/ruby.json
```

## JSON Fields

| Field | Type | Required | Default | Description |
|-------|------|:--------:|---------|-------------|
| `material` | Item ID or Ingredient | ✅ | — | The crafting material. Can be a plain item ID or an ingredient object (for tags). |
| `baseDamage` | `double` | ✅ | — | Base damage on hit before enchantments. Vanilla flint arrow is `2.0`. |
| `color` | `int` | ✅ | — | ARGB tint color as a **decimal** integer. |
| `translationKey` | `string` | ✅ | — | Translation key for the arrow's display name. |
| `flame` | `boolean` | ✅ | `false` | Whether the arrow sets the target on fire on hit. |
| `gravity` | `double` | ✅ | `0.05` | Gravity factor on the arrow's arc. Lower = flatter. |
| `effects` | `Map<string, int>` | ✅ | `{}` | Potion effects on hit. Key = effect ID, value = amplifier. |
| `feather` | Item ID | ✅ | `"minecraft:feather"` | Must be `"minecraft:feather"` or `"arrowplus:gilded_feather"`. |
| `stick` | Item ID | ✅ | `"minecraft:stick"` | Must be `"minecraft:stick"` or one of the Arrow+ stick item IDs. |
| `outputAmount` | `int` | ✅ | `4` | Number of arrows produced per craft. |

::: warning Stick and Feather Restrictions
In the legacy system, the `stick` field **must** be one of:
- `minecraft:stick`
- `arrowplus:copper_stick`
- `arrowplus:iron_stick`
- `arrowplus:gold_stick`
- `arrowplus:diamond_stick`
- `arrowplus:emerald_stick`
- `arrowplus:netherite_stick`

The `feather` field **must** be one of:
- `minecraft:feather`
- `arrowplus:gilded_feather`

Any other item ID will be rejected with an error. **There is no `stickData` or `featherData` field in this version.**
:::

## Examples

### Simple arrow (vanilla ingredients)

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

### Arrow with a material stick

```json
{
  "material": "minecraft:iron_ingot",
  "baseDamage": 2.5,
  "color": -5263441,
  "translationKey": "item.arrowplus.iron_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "minecraft:feather",
  "stick": "arrowplus:copper_stick",
  "outputAmount": 4
}
```

### Arrow with gilded feather

```json
{
  "material": "minecraft:obsidian",
  "baseDamage": 3.8,
  "color": -13729721,
  "translationKey": "item.arrowplus.obsidian_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "arrowplus:gilded_feather",
  "stick": "arrowplus:diamond_stick",
  "outputAmount": 2
}
```

### Arrow using a tag as material

```json
{
  "material": {
    "tag": "minecraft:stone_crafting_materials"
  },
  "baseDamage": 1.5,
  "color": -11776183,
  "translationKey": "item.arrowplus.stone_arrow",
  "flame": false,
  "gravity": 0.05,
  "effects": {},
  "feather": "minecraft:feather",
  "stick": "minecraft:stick",
  "outputAmount": 8
}
```

## Lang File

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.ruby_arrow": "Ruby Arrow"
}
```
