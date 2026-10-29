# Feathers

The feather sets how far and how straight an arrow flies, and can counterbalance a heavy shaft. Any feather works with any head and stick.

| Stat | Meaning |
| --- | --- |
| **Gravity multiplier** | Multiplies the shaft’s gravity. Phantom membrane uses `0.6`, reducing drop by 40%. |
| **Air drag** | Speed lost each tick in air. Vanilla is 1%, lower flies further. |
| **Water drag** | Speed lost each tick in water. Vanilla is 40%, which is why arrows stop almost at once underwater. |
| **Spread** (`accuracy`) | Multiplier on shot spread: `1` is vanilla, below `1` is tighter, and above `1` is wider. |

## All feathers

<PartTable kind="feather" />

## Feathers used as they are

The vanilla feather, phantom membrane and paper go straight into the arrow recipe. The vanilla feather entry matches the `#c:feathers` tag, so feathers from other mods work too.

<RecipeCard id="emerald" feather="phantom_membrane" />

## Crafted feathers

The Gilded, Kelp and Prismarine Feathers are crafted first. Surround a vanilla feather with four of the material to make four:

<RecipeCard type="feather" id="kelp" />
<RecipeCard type="feather" id="prismarine" />

Kelp and Prismarine Feathers keep arrows flying underwater. They are all one item, `arrowplus:custom_feather`, and the feather type comes from its `arrowplus:feather_data` component, which points at a [feather data](./datapack/feather-data) entry.

<RecipeCard id="prismarine" feather="kelp" />

## Add your own

See [Feather data](./datapack/feather-data) to add feathers, or to change the stats of the built-in ones.
