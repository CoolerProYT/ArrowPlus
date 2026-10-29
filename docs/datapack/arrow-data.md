# Arrow data

Every arrow head is an entry in the `arrowplus/arrows` datapack registry. A datapack can add new heads, or change built-in ones by overwriting their file.

```
data/<namespace>/arrowplus/arrows/<name>.json
```

For example, `data/mymod/arrowplus/arrows/ruby.json` adds the head `mymod:ruby`. The mod's own heads live in `data/arrowplus/arrowplus/arrows/`.

A head only sets damage, flame and effects. How the arrow flies comes from the [stick](./stick-data) and [feather](./feather-data) the player crafts it with, so every head works with every stick and feather.

## Arrow designer

Pick the stats and the colors, and copy the two files. The preview uses the mod's own textures, tinted the same way the game does it. The stick and feather only change the preview.

<ArrowDesigner />

## Fields

| Field | Type | Required | Description |
| --- | --- | :---: | --- |
| `material` | item id, `#tag` or list | ✅ | The top item of the recipe. |
| `baseDamage` | number | ✅ | Damage before speed, crits and Power. The vanilla Flint Arrow does `2.0`. See [damage](../arrows#damage). |
| `color` | int | ✅ | Color of the arrow head, as a **signed decimal ARGB** int. `-12532481` is `#40C4FF`. The designer above converts for you. |
| `translationKey` | string | ✅ | Lang key for the arrow's name, e.g. `item.mymod.ruby_arrow`. |
| `flame` | boolean | ✅ | Whether the arrow sets the target on fire. |
| `effects` | object | ✅ | Potions applied on hit: potion id → duration in ticks. `{}` for none. |
| `outputAmount` | int | | Arrows per craft. Defaults to `4`. The shipped non-flint Fletching Table recipes make double; custom heads need their own compatible Fletching Recipe recipe. |

## Examples

### Plain head

<RecipeCard id="copper" />

```json
{
  "material": "minecraft:copper_ingot",
  "baseDamage": 2.2,
  "color": -2855612,
  "translationKey": "item.arrowplus.copper_arrow",
  "flame": false,
  "effects": {},
  "outputAmount": 4
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
  "effects": {},
  "outputAmount": 8
}
```

### Flame head with an effect

A new head that sets targets on fire and slows them for 5 seconds (100 ticks), using the effects of the `minecraft:slowness` potion:

```json
{
  "material": "minecraft:magma_cream",
  "baseDamage": 2.5,
  "color": -1497344,
  "translationKey": "item.mymod.magma_arrow",
  "flame": true,
  "effects": {
    "minecraft:slowness": 100
  },
  "outputAmount": 4
}
```

## Name

Add the `translationKey` to a resource pack's lang file:

```json
// assets/mymod/lang/en_us.json
{
  "item.mymod.magma_arrow": "Magma Arrow"
}
```

## Migrating from 26.3.0.1

Before 26.3.0.2, every arrow had a fixed stick and feather. These fields still load, but are **deprecated** and will be removed in a future version:

| Old field | Now |
| --- | --- |
| `gravity` | Ignored. Gravity belongs to the [stick](./stick-data). |
| `stick`, `stickData` | No longer restricts crafting. Read only to migrate old stacks to stick components. |
| `feather`, `featherData` | No longer restricts crafting. Read only to migrate old stacks to feather components. |

When the world starts, the log names every head that still uses them:

```
[arrowplus] Arrow mymod:ruby uses deprecated fields (gravity, feather, featherData, stick, stickData). ...
```

To migrate, delete those fields. If your arrow needed a special `gravity`, make a [stick](./stick-data) with that gravity instead.

Arrows crafted on an older version are converted when their item stacks are loaded, including arrows inside chests, shulkers, bundles and loaded crossbows. Counts, potion contents, names and custom data are preserved. Migration only fills missing part components; an existing shaft or feather selection is never replaced.

For missing parts, explicit legacy `stickData`/`featherData` or standalone `stick`/`feather` fields take priority. If those fields are absent, built-in heads use their original recipe parts:

| Old head | Shaft | Fletching |
| --- | --- | --- |
| Iron | Copper stick | Feather |
| Gold | Iron stick | Feather |
| Diamond | Gold stick | Feather |
| Emerald | Diamond stick | Feather |
| Obsidian, Netherite | Diamond stick | Gilded Feather |
| Other built-in heads | Stick | Feather |

Custom heads without legacy part fields fall back to the vanilla stick and feather. If a custom datapack used special parts, keep its legacy fields until the old stacks have migrated; removed custom recipe information cannot be reconstructed. Migrated parts use the current flight stats.

Already-converted stacks with explicit vanilla parts are kept as they are, since they cannot be distinguished from intentionally crafted vanilla-part builds.

## Disabling built-in heads

To turn off a built-in head without a datapack, list it in `restrictions` in the [config](../config).
