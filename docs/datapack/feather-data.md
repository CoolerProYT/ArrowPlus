# Feather data

Every custom feather is an entry in the `arrowplus/feathers` datapack registry. Add your own to use them in [arrow recipes](./arrow-data).

```
data/<namespace>/arrowplus/feathers/<name>.json
```

For example, `data/mymod/arrowplus/feathers/spectral.json` adds the feather `mymod:spectral`.

## Fields

| Field | Type | Required | Description |
| --- | --- | :---: | --- |
| `material` | item id, `#tag` or list of item ids | ✅ | What the feather is crafted from. |
| `color` | int | ✅ | Tint of the feather, as a **signed decimal ARGB** int. The [arrow designer](./arrow-data#arrow-designer) converts colors. |
| `translationKey` | string | ✅ | Lang key for the feather's name. |
| `outputAmount` | int | | Feathers per craft. Defaults to `4`. |

## Crafting

Four of the material around a vanilla feather make `outputAmount` custom feathers:

<RecipeCard type="feather" id="gilded" />

## Example

```json
{
  "material": "minecraft:glow_ink_sac",
  "color": -16711681,
  "translationKey": "item.mymod.spectral_feather",
  "outputAmount": 4
}
```

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.spectral_feather": "Spectral Feather"
}
```

Then use it in an arrow entry:

```json
{
  "feather": "arrowplus:custom_feather",
  "featherData": "mymod:spectral"
}
```

## Built-in entries

<PartTable kind="feather" />
