# Getting started

Arrow+ adds 18 new arrows to Minecraft, from the cheap <ItemSlot id="arrowplus:wood_arrow" label /> to the <ItemSlot id="arrowplus:netherite_arrow" label />. Every arrow, stick and feather is a datapack entry, so you can change them or add your own.

::: info You are viewing the new system docs
This covers **26.1+** (NeoForge and Fabric) and **1.21.1-NeoForge**, where arrows, sticks and feathers are all datapack driven.

On **1.21.11**, **1.21.10**, **1.21.8**, **1.20.1** or any other older version, use the version switcher at the top to open the [legacy docs](/legacy/getting-started).
:::

## Install

1. Install [Fabric](https://fabricmc.net/) with Fabric API, or [NeoForge](https://neoforged.net/).
2. Download Arrow+ from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/arrow) or [Modrinth](https://modrinth.com/mod/srzQqcGY) and put the jar in your `mods` folder.
3. Optional: add [JEI](https://modrinth.com/mod/jei) or [REI](https://modrinth.com/mod/rei) to see every arrow, stick and feather recipe in game, and [Fletching Recipe](https://www.curseforge.com/minecraft/mc-mods/fletching-recipe) to craft arrows at the Fletching Table for double the output.

| Mod loader | Minecraft | Status |
| --- | :---: | :---: |
| NeoForge, Fabric | 26.1 and newer | :white_check_mark: Maintained |
| NeoForge | 1.21.1 | Partial |

::: warning
Only the latest 26.x release is actively maintained. `1.21.1-NeoForge` uses the same datapack system but is not actively maintained.
:::

## Your first arrows

Arrows are crafted from a **material**, a **stick** and a **feather**, stacked in one column of a Crafting Table. The cheaper arrows only need a vanilla stick and feather:

<RecipeCard id="copper" />

Stronger arrows need a [custom stick](./custom-sticks), and the best two also need the [Gilded Feather](./custom-feathers):

<RecipeCard id="iron" />

<RecipeCard id="netherite" />

See every arrow, its damage and its recipe on the [Arrows](./arrows) page.

## How it works

All Arrow+ arrows are one item, `arrowplus:arrow_plus`. What kind of arrow a stack is comes from a data component, pointing at a datapack entry:

| Component | Item | Datapack entries |
| --- | --- | --- |
| `arrowplus:arrow_data` | `arrowplus:arrow_plus` | [`arrowplus/arrows/`](./datapack/arrow-data) |
| `arrowplus:stick_data` | `arrowplus:custom_stick` | [`arrowplus/sticks/`](./datapack/stick-data) |
| `arrowplus:feather_data` | `arrowplus:custom_feather` | [`arrowplus/feathers/`](./datapack/feather-data) |

This means any arrow, stick or feather can be added, changed or removed with a datapack, with no Java code.

## Recipe viewers

Arrow+ has built-in **JEI** and **REI** support for arrow, stick, feather and tipped arrow recipes. Hold **Shift** over an arrow to see its damage and other [stats](./arrows#tooltip).

## Changelog

::: details 26.2.0.1
#### Fixes
- The two extra arrows from a Multishot crossbow can no longer be picked up
- Arrows shot in Creative mode can no longer be picked up
:::

::: details 26.1.2.102
#### Changes
- Added `arrow_plus` to the `#arrows` entity type tag
:::

::: details 26.1.2.101
#### Fixes
- Removed the arrow info tooltip from other mods' arrows
:::

::: details 26.1.2.100
#### Additions
- Config to hide tipped arrows from JEI/REI
- Stick and feather recipe types for the Crafting Table
- JEI/REI support for custom sticks and feathers
- Arrow data in the tooltip (hold **Shift**)

#### Changes
- Sticks and feathers are now **datapack driven**: all individual stick items and `gilded_feather` were replaced by one `custom_stick` and one `custom_feather` item
- Arrow data `stick`/`feather` fields that use `arrowplus:custom_stick` / `arrowplus:custom_feather` now need a matching `stickData` / `featherData` field

#### Deprecation
- The individual stick items (`copper_stick`, `iron_stick`, `gold_stick`, `diamond_stick`, `emerald_stick`, `netherite_stick`) and `gilded_feather` are deprecated. They still exist but will be removed in a future version.

#### Fixes
- Fixed a duplicated tipped arrow effect tooltip
- Tipped custom arrows are no longer affected by Infinity
:::
