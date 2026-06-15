## 26.1.2.101
- Removed arrow info tooltip for other modded arrow

## 26.1.2.100
- Fixed duplicated tipped arrow effect tooltip
- Tipped custom arrow no longer affected by infinity
- Added arrow data info to arrows tooltip
- Added config to hide tipped arrow from JEI/REI
- Sticks and feathers are now datapack-driven (like arrows), replaced individual stick items and gilded feather with a single `custom_stick` and `custom_feather` item
- Deprecated individual stick items (copper_stick, iron_stick, gold_stick, diamond_stick, emerald_stick, netherite_stick) and gilded_feather (will be removed in a future version)
- Added stick and feather recipe types for crafting table
- Added JEI/REI support for custom sticks and feathers
- Arrow data `stick` and `feather` fields that reference `arrowplus:custom_stick` / `arrowplus:custom_feather` now require a `stickData` / `featherData` field pointing to the corresponding datapack entry (e.g. `"stickData": "arrowplus:gold"`)