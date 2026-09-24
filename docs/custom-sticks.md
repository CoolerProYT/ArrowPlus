# Custom sticks

The stronger arrows need a metal or gem stick instead of a plain one. All of them are one item, `arrowplus:custom_stick`, and the stick type comes from its `arrowplus:stick_data` component, which points at a [stick data](./datapack/stick-data) entry.

## Built-in sticks

<PartTable kind="stick" />

## Crafting

Two of the material in a column make four sticks:

<RecipeCard type="stick" id="copper" />
<RecipeCard type="stick" id="diamond" />

Each stick type uses its own material. JEI and REI show all of them.

::: info Deprecated items
The old item ids (`arrowplus:copper_stick`, `arrowplus:iron_stick` and so on) still exist in 26.1+ but are **deprecated** and will be removed in a future version. Use `arrowplus:custom_stick` with a `stick_data` component.
:::

## In arrow datapacks

When an arrow entry uses `arrowplus:custom_stick`, it **must** also say which stick with `stickData`:

```json
{
  "stick": "arrowplus:custom_stick",
  "stickData": "arrowplus:copper"
}
```

See [Arrow data](./datapack/arrow-data) for the full format, and [Stick data](./datapack/stick-data) to add your own stick types.
