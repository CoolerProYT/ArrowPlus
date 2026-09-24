# Custom feathers

The two strongest arrows need the <ItemSlot id="arrowplus:gilded_feather" label /> instead of a normal feather. Custom feathers are one item, `arrowplus:custom_feather`, and the feather type comes from its `arrowplus:feather_data` component, which points at a [feather data](./datapack/feather-data) entry.

## Built-in feathers

<PartTable kind="feather" />

## Crafting

Surround a vanilla feather with four of the material:

<RecipeCard type="feather" id="gilded" />

::: info Deprecated item
The old `arrowplus:gilded_feather` item still exists in 26.1+ but is **deprecated** and will be removed in a future version. Use `arrowplus:custom_feather` with `featherData: "arrowplus:gilded"`.
:::

## In arrow datapacks

When an arrow entry uses `arrowplus:custom_feather`, it **must** also say which feather with `featherData`:

```json
{
  "feather": "arrowplus:custom_feather",
  "featherData": "arrowplus:gilded"
}
```

See [Arrow data](./datapack/arrow-data) for the full format, and [Feather data](./datapack/feather-data) to add your own feather types.
