# Sticks

The stick sets how an arrow flies through the air. Any stick works with any head and feather.

| Stat | Meaning |
| --- | --- |
| **Gravity** | How fast the arrow drops each tick. Vanilla is `0.05`, lower is a flatter arc. |
| **Velocity** | Launch speed multiplier. Faster arrows fly further and [hit harder](./arrows#damage). |

Final gravity is the shaft’s gravity multiplied by the feather’s gravity multiplier. Copper and iron extend the range of a wooden shaft. Netherite launches harder but drops sharply; phantom membrane reduces its drop. Bamboo trades launch speed and damage for lower gravity.

The in-game **Arrow drop** multiplier is final gravity divided by the vanilla value `0.05`. The table below lists shaft gravity before the feather modifier.

## All sticks

<PartTable kind="stick" />

## Sticks used as they are

The vanilla stick, bamboo, bone, blaze rod and breeze rod go straight into the arrow recipe. The vanilla stick entry matches the `#c:rods/wooden` tag, so wooden rods from other mods work too.

<RecipeCard id="iron" stick="bamboo" />

## Crafted sticks

The metal and gem sticks are crafted first. Two of the material in a column make four:

<RecipeCard type="stick" id="iron" />
<RecipeCard type="stick" id="netherite" />

They are all one item, `arrowplus:custom_stick`, and the stick type comes from its `arrowplus:stick_data` component, which points at a [stick data](./datapack/stick-data) entry. JEI shows every recipe.

<RecipeCard id="gold" stick="diamond" />

## Add your own

See [Stick data](./datapack/stick-data) to add sticks, or to change the stats of the built-in ones.
