# Material Sticks

On legacy versions (1.21.11-NeoForge and older), Arrow+ registers **6 individual stick items** and **1 special feather item**. These are distinct item IDs — not datapack entries.

::: info Version Difference
In the new system (26.1+ / 1.21.1-NeoForge), these are replaced by a single `arrowplus:custom_stick` and `arrowplus:custom_feather` item driven by datapack registries. On legacy versions, each stick/feather type is a separate registered item.
:::

## Stick Items

| Item ID | Display Name | Material | Output |
|---------|--------------|----------|:------:|
| <ItemSlot id="arrowplus:copper_stick" /> `arrowplus:copper_stick` | Copper Stick | Copper Ingot | 4 |
| <ItemSlot id="arrowplus:iron_stick" /> `arrowplus:iron_stick` | Iron Stick | Iron Ingot | 4 |
| <ItemSlot id="arrowplus:gold_stick" /> `arrowplus:gold_stick` | Gold Stick | Gold Ingot | 4 |
| <ItemSlot id="arrowplus:diamond_stick" /> `arrowplus:diamond_stick` | Diamond Stick | Diamond | 4 |
| <ItemSlot id="arrowplus:emerald_stick" /> `arrowplus:emerald_stick` | Emerald Stick | Emerald | 4 |
| <ItemSlot id="arrowplus:netherite_stick" /> `arrowplus:netherite_stick` | Netherite Stick | Netherite Ingot | 4 |

## Crafting Recipe

All material sticks are crafted in a **2×1 column**:

```
[ Material ]
[ Material ]
```

## Gilded Feather

| Item ID | Display Name | Material | Output |
|---------|--------------|----------|:------:|
| <ItemSlot id="arrowplus:gilded_feather" /> `arrowplus:gilded_feather` | Gilded Feather | Glowstone Dust + Feather | 4 |

## Usage in Arrow Datapacks

When writing a legacy arrow datapack entry, reference these items directly by their item ID in the `stick` and `feather` fields:

```json
{
  "stick": "arrowplus:copper_stick",
  "feather": "minecraft:feather"
}
```

See [Arrow Data](/legacy/datapack/arrow-data) for the full format and restrictions.
