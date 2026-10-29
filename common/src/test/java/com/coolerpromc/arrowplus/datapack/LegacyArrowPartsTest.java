package com.coolerpromc.arrowplus.datapack;

import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyArrowPartsTest {
    @Test
    void preservesTheOriginalMetalShaftRecipes() {
        assertEquals(Sticks.COPPER, LegacyArrowParts.stick(Identifier.parse("arrowplus:iron")));
        assertEquals(Sticks.IRON, LegacyArrowParts.stick(Identifier.parse("arrowplus:gold")));
        assertEquals(Sticks.GOLD, LegacyArrowParts.stick(Identifier.parse("arrowplus:diamond")));
        for (String head : new String[]{"emerald", "obsidian", "netherite"}){
            assertEquals(Sticks.DIAMOND, LegacyArrowParts.stick(Identifier.parse("arrowplus:" + head)));
        }
    }

    @Test
    void preservesGildedFletchingOnObsidianAndNetherite() {
        for (String head : new String[]{"obsidian", "netherite"}){
            assertEquals(Feathers.GILDED, LegacyArrowParts.feather(Identifier.parse("arrowplus:" + head)));
        }
        assertEquals(Feathers.FEATHER, LegacyArrowParts.feather(Identifier.parse("arrowplus:diamond")));
    }

    @Test
    void doesNotApplyBuiltInRecipesToOtherNamespaces() {
        assertEquals(Sticks.STICK, LegacyArrowParts.stick(Identifier.parse("custom:iron")));
        assertEquals(Feathers.FEATHER, LegacyArrowParts.feather(Identifier.parse("custom:netherite")));
        assertEquals(Sticks.STICK, LegacyArrowParts.stick(Identifier.parse("arrowplus:flint")));
        assertEquals(Sticks.STICK, LegacyArrowParts.stick(Identifier.parse("arrowplus:copper")));
    }
}
