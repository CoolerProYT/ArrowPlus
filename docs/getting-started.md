# Getting started

Arrow+ lets you build your own arrows. Every arrow is a **head**, a **stick** and a **feather**, and any combination works: a <ItemSlot id="arrowplus:diamond_arrow" label /> can use a plain stick, bamboo for lower gravity and speed, or a blaze rod to hit harder. Every head, stick and feather is a datapack entry, so you can change them or add your own.

::: info You are viewing the modular arrow docs
This covers **Arrow+ 26.3.0.2 and newer** for **Minecraft 26.3** on NeoForge and Fabric.

On **Arrow+ 26.1 to 26.3.0.1** or **1.21.1-NeoForge**, arrows have fixed recipes: see the [26.1 – 26.3.0.1 docs](/v26-1/getting-started). On **1.21.11** or older, see the [legacy docs](/legacy/getting-started). The version switcher at the top moves between them.
:::

## Install

1. Install [Fabric](https://fabricmc.net/) with Fabric API, or [NeoForge](https://neoforged.net/).
2. Download Arrow+ from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/arrow) or [Modrinth](https://modrinth.com/mod/srzQqcGY) and put the jar in your `mods` folder.
3. Optional: add [JEI](https://modrinth.com/mod/jei) to see every arrow, stick and feather recipe in game, and [Fletching Recipe](https://www.curseforge.com/minecraft/mc-mods/fletching-recipe) to craft arrows at the Fletching Table for double the output.

| Mod loader | Minecraft | Arrow+ version | Docs |
| --- | :---: | :---: | --- |
| NeoForge, Fabric | 26.3 | 26.3.0.2+ | Modular arrows, maintained |
| NeoForge, Fabric | 26.1–26.3 | 26.1 to 26.3.0.1 | [Fixed recipes](/v26-1/getting-started) |
| NeoForge | 1.21.1 | 1.21.1 builds | [Fixed recipes](/v26-1/getting-started), partial |

::: warning
Only the latest 26.x release is actively maintained.
:::

## Your first arrows

Put a head, a stick and a feather in one column of a Crafting Table, top to bottom. With a plain stick and feather you get a normal arrow with the head's damage:

<RecipeCard id="copper" />

Swap the stick or feather for a different flight. Bamboo reduces gravity at the cost of launch speed and damage. Phantom membrane reduces the shaft’s gravity further and tightens spread:

<RecipeCard id="diamond" stick="bamboo" feather="phantom_membrane" />

| Part | What it changes |
| --- | --- |
| [Head](./arrows) | Damage, flame and potion effects |
| [Stick](./custom-sticks) | Gravity (how fast it drops) and velocity (launch speed, which also adds damage) |
| [Feather](./custom-feathers) | Air drag, water drag, spread and a gravity multiplier |

Try combinations with the [arrow builder](./arrows#arrow-builder). Arrows stack when their head, stick, feather, potion contents and other item data match.

## How it works

All Arrow+ arrows are one item, `arrowplus:arrow_plus`. Each arrow stack carries three data components, one per part, each pointing at a datapack entry:

| Component | Part | Datapack entries |
| --- | --- | --- |
| `arrowplus:arrow_data` | Head | [`arrowplus/arrows/`](./datapack/arrow-data) |
| `arrowplus:stick_data` | Stick | [`arrowplus/sticks/`](./datapack/stick-data) |
| `arrowplus:feather_data` | Feather | [`arrowplus/feathers/`](./datapack/feather-data) |

Some sticks and feathers are plain items used as they are, like bamboo or phantom membrane. Others are crafted into `arrowplus:custom_stick` or `arrowplus:custom_feather`, which carry the same `stick_data` or `feather_data` component. Any head, stick or feather can be added, changed or removed with a datapack, with no Java code.

## Recipe viewers and tooltips

Arrow+ has built-in **JEI** support for arrow, stick, feather and tipped-arrow recipes. Looking up a crafted arrow with custom parts shows the recipe for that exact build. Fletching Recipe's potion and explosive variants remain distinct in JEI.

Arrow tooltips show a compact build, damage and speed summary. Hold **Shift** for **Combat** and **Flight** [stats](./arrows#tooltip). Ingredients show **Arrow material** and **Hold [Shift] for stats**; Shift reveals their roles and stats.

## Changelog

::: details Current release
<!--@include: ../changelog.md-->
:::

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
