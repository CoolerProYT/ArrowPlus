# Arrows

Every Arrow+ arrow is a **head**, a **stick** and a **feather**. They all share one item, `arrowplus:arrow_plus`, and work in bows, crossbows and dispensers like a vanilla arrow.

| Part | Gives |
| --- | --- |
| Head | Base damage, flame and potion effects |
| [Stick](./custom-sticks) | Gravity and velocity |
| [Feather](./custom-feathers) | Air drag, water drag, accuracy and a gravity multiplier |

## Arrow builder

Pick a head, stick and feather to see the arrow they make:

<ArrowBuilder />

## Heads

Arrow+ has 19 heads. The values below are read from the mod's built-in datapack.

<ArrowTable />

## Crafting

Put the head's material, a stick and a feather in one column of a Crafting Table, top to bottom. Any stick and feather works:

<RecipeCard id="iron" />

<RecipeCard id="netherite" stick="blaze_rod" feather="kelp" />

With the [Fletching Recipe](https://www.curseforge.com/minecraft/mc-mods/fletching-recipe) mod installed, the 18 non-flint built-in heads can also be made at a <ItemSlot id="minecraft:fletching_table" label /> with a plain stick and feather, for **double the output**:

<RecipeCard id="diamond" fletching />

Some heads accept any item from a tag. The slot cycles through them, like in JEI:

<RecipeCard id="wood" />

<RecipeCard id="stone" />

Flint is a head too, for arrows with any other stick or feather. Flint, a plain stick and a plain feather still make the vanilla arrow.

Arrows only stack when their head, stick, feather, potion contents and other item data match.

## Damage

Before crits and enchantments, arrows hit for **impact speed × base damage**, rounded up. A fully drawn bow launches at 3 blocks per tick, so a vanilla arrow (2.0) has a point-blank hit of 6 before crits and enchantments. A stick's velocity changes the launch speed, so a faster stick also hits harder: a Diamond Arrow (3.5) does 11 with a plain stick and 10 with bamboo (×0.9).

Arrows slow down as they fly, by the feather's air or water drag, so far shots hit softer. Power, crits and potion effects work as in vanilla.

## Example builds

- **Heavy hitter:** any damage head + netherite shaft + ordinary feather. High launch damage with a steep arc.
- **Counterbalanced:** the same head + netherite shaft + phantom membrane. The membrane reduces gravity by 40% and keeps more speed in flight.
- **Diving arrow:** any head + iron shaft + kelp feather. Kelp keeps 95% of speed per tick underwater, at the cost of air drag and accuracy.

## Tooltip

The compact tooltip names the shaft and fletching and shows base damage and launch speed. Hold **Shift** for two sections:

| Section | Shows |
| --- | --- |
| **Combat** | Base damage, Infinity support, and ignition when the head sets targets on fire. |
| **Flight** | Launch speed, combined arrow drop, air and water speed loss per tick, and spread. |

Multipliers use **×1 as the vanilla baseline**. For example, drop `×0.54` means 46% less downward acceleration; spread `×0.8` means 20% less spread. The drop value already combines the shaft and fletching. Green values improve on vanilla, red values are worse, and unchanged values are white. Head effects appear under **On hit**; tipped arrows also show their potion effects.

Ingredients such as diamonds, bamboo and dried kelp show **Arrow material** and **Hold [Shift] for stats**. Hold **Shift** to see their roles and stats.

## JEI recipe lookup

Press **R** on a modular arrow to see the recipe for its exact head, stick and feather. For a tipped arrow, the recipe uses eight matching untipped arrows and the appropriate lingering potion. Usage lookups on an untipped modular arrow also show its tipping recipes.

## Existing arrows

Older built-in arrows recover the shaft and fletching from their original recipes when missing part components are migrated. Existing modular builds keep their selected parts, and counts and potion contents are preserved. Arrows still use the current balance values. See [migration details](./datapack/arrow-data#migrating-from-26-3-0-1) for the historical mappings and custom datapack behavior.

## Infinity

A bow with **Infinity** does not use up Arrow+ arrows, just like vanilla arrows. Specific heads can be excluded with `infinityBlacklist` in the [config](./config).

::: warning
[Tipped arrows](./tipped-arrows) are **never** kept by Infinity, matching vanilla.
:::
