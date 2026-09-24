# Arrows

Arrow+ adds 18 new arrow types on legacy versions. All arrows share the same item (`arrowplus:arrow_plus`) and are distinguished by their `arrowplus:arrow_data` data component.

## Crafting Recipe

Arrows are crafted in a **3×1 vertical column** in a Crafting Table:

```
[ Material ]
[  Stick   ]
[ Feather  ]
```

If the [Fletching Recipe](https://www.curseforge.com/minecraft/mc-mods/fletching-recipe) mod is installed, arrows can also be crafted at a Fletching Table for **double the output**.

## Arrow List

| Arrow | Material | Stick | Feather | Output | Base Damage |
|-------|----------|-------|---------|:------:|:-----------:|
| <ItemSlot id="arrowplus:wood_arrow" label /> | Any Planks *(tag)* | Stick | Feather | 8 | 1.0 |
| <ItemSlot id="arrowplus:stone_arrow" label /> | Stone / Cobblestone / etc. *(tag)* | Stick | Feather | 8 | 1.5 |
| <ItemSlot id="arrowplus:bone_arrow" label /> | Bone | Stick | Feather | 8 | 1.8 |
| <ItemSlot id="arrowplus:copper_arrow" label /> | Copper Ingot | Stick | Feather | 4 | 2.2 |
| <ItemSlot id="arrowplus:lapis_arrow" label /> | Lapis Lazuli | Stick | Feather | 4 | 2.3 |
| <ItemSlot id="arrowplus:brick_arrow" label /> | Brick | Stick | Feather | 8 | 2.3 |
| <ItemSlot id="arrowplus:glowstone_arrow" label /> | Glowstone Dust | Stick | Feather | 6 | 2.0 |
| <ItemSlot id="arrowplus:redstone_arrow" label /> | Redstone | Stick | Feather | 4 | 1.8 |
| <ItemSlot id="arrowplus:charcoal_arrow" label /> | Charcoal | Stick | Feather | 6 | 1.6 |
| <ItemSlot id="arrowplus:quartz_arrow" label /> | Nether Quartz | Stick | Feather | 6 | 2.7 |
| <ItemSlot id="arrowplus:prismarine_arrow" label /> | Prismarine Shard | Stick | Feather | 6 | 2.8 |
| <ItemSlot id="arrowplus:amethyst_arrow" label /> | Amethyst Shard | Stick | Feather | 4 | 3.2 |
| <ItemSlot id="arrowplus:gold_arrow" label /> | Gold Ingot | <ItemSlot id="arrowplus:iron_stick" label /> | Feather | 4 | 2.0 |
| <ItemSlot id="arrowplus:iron_arrow" label /> | Iron Ingot | <ItemSlot id="arrowplus:copper_stick" label /> | Feather | 4 | 2.5 |
| <ItemSlot id="arrowplus:emerald_arrow" label /> | Emerald | <ItemSlot id="arrowplus:diamond_stick" label /> | Feather | 3 | 3.0 |
| <ItemSlot id="arrowplus:diamond_arrow" label /> | Diamond | <ItemSlot id="arrowplus:gold_stick" label /> | Feather | 3 | 3.5 |
| <ItemSlot id="arrowplus:obsidian_arrow" label /> | Obsidian | <ItemSlot id="arrowplus:diamond_stick" label /> | <ItemSlot id="arrowplus:gilded_feather" label /> | 2 | 3.8 |
| <ItemSlot id="arrowplus:netherite_arrow" label /> | Netherite Ingot | <ItemSlot id="arrowplus:diamond_stick" label /> | <ItemSlot id="arrowplus:gilded_feather" label /> | 2 | 4.5 |

::: tip
The metal sticks and the Gilded Feather are Arrow+ items. See [Material Sticks](/legacy/material-sticks) for crafting details.
:::

## Arrow Tooltip

Hovering an arrow shows its `Base Damage` in the item tooltip.

| Property | Description |
|----------|-------------|
| **Base Damage** | Damage on hit before bow enchantments |
| **Flame** | Whether the arrow sets the target on fire |
| **Gravity** | Trajectory arc factor |

## Infinity Enchantment

On legacy versions, **all** Arrow+ arrows (including tipped ones) are compatible with the Infinity enchantment. There is no per-arrow infinity blacklist — only the `restrictions` list in [Config](/legacy/config) exists.
