# Arrows

Arrow+ adds 18 arrows. They all share one item, `arrowplus:arrow_plus`, and differ by their `arrowplus:arrow_data` component. Each one works in bows, crossbows and dispensers like a vanilla arrow, but hits for its own **base damage**.

## All arrows

The values below are read from the mod's built-in datapack. Arrows with a highlighted stick or feather need a [custom stick](./custom-sticks) or [custom feather](./custom-feathers).

<ArrowTable />

## Crafting

Put the material, stick and feather in one column of a Crafting Table, top to bottom:

<RecipeCard id="diamond" fletching />

With the [Fletching Recipe](https://www.curseforge.com/minecraft/mc-mods/fletching-recipe) mod installed, every arrow can also be made at a <ItemSlot id="minecraft:fletching_table" label /> for **double the output**, as shown on the right.

Some materials accept any item from a tag. The slot cycles through them, like in JEI:

<RecipeCard id="wood" />

<RecipeCard id="stone" />

## Tooltip

Hold **Shift** while hovering over an arrow to see its data:

| Property | Meaning |
| --- | --- |
| **Base Damage** | Damage on hit before bow enchantments. The vanilla <ItemSlot id="minecraft:arrow" /> Flint Arrow does 2.0. |
| **Flame** | Whether the arrow sets the target on fire |
| **Gravity** | How fast the arrow drops. Lower is a flatter arc. |
| **Affected by Infinity** | Whether the Infinity enchantment keeps the arrow |

## Infinity

A bow with **Infinity** does not use up Arrow+ arrows, just like vanilla arrows. Specific arrows can be excluded with `infinityBlacklist` in the [config](./config).

::: warning
[Tipped arrows](./tipped-arrows) are **never** kept by Infinity, matching vanilla.
:::
