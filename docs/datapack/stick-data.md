# Stick data

Every custom stick is an entry in the `arrowplus/sticks` datapack registry. Add your own to use them in [arrow recipes](./arrow-data).

```
data/<namespace>/arrowplus/sticks/<name>.json
```

For example, `data/mymod/arrowplus/sticks/amethyst.json` adds the stick `mymod:amethyst`.

## Fields

| Field | Type | Description |
| --- | --- | --- |
| `material` | item id, `#tag` or list of item ids | What the stick is crafted from. |
| `color` | int | Tint of the stick, as a **signed decimal ARGB** int. The [arrow designer](./arrow-data#arrow-designer) converts colors. |
| `translationKey` | string | Lang key for the stick's name. |
| `outputAmount` | int | Sticks per craft. Built-in sticks make `4`. |

## Crafting

Two of the material in a column make `outputAmount` sticks:

<RecipeCard type="stick" id="emerald" />

## Example

```json
{
  "material": "minecraft:amethyst_shard",
  "color": -6543440,
  "translationKey": "item.mymod.amethyst_stick",
  "outputAmount": 4
}
```

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.amethyst_stick": "Amethyst Stick"
}
```

Then use it in an arrow entry:

```json
{
  "stick": "arrowplus:custom_stick",
  "stickData": "mymod:amethyst"
}
```

## Built-in entries

<PartTable kind="stick" />
