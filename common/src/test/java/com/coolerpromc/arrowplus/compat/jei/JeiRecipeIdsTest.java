package com.coolerpromc.arrowplus.compat.jei;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JeiRecipeIdsTest {
    private static Identifier id(String name) {
        return Identifier.parse(name);
    }

    @Test
    void tippedRecipesDistinguishEveryHeadAndPotion() {
        Set<Identifier> recipes = new HashSet<>();
        for (String head : new String[]{"arrowplus:diamond", "arrowplus:iron", "custom:diamond"}){
            for (String potion : new String[]{"minecraft:poison", "minecraft:healing", "custom:poison"}){
                assertTrue(recipes.add(JeiRecipeIds.tipped(id(head), id(potion))));
            }
        }
        assertEquals(9, recipes.size());
    }

    @Test
    void namespacedAssemblyRecipesStaySeparate() {
        assertNotEquals(JeiRecipeIds.arrow(id("arrowplus:iron")), JeiRecipeIds.arrow(id("custom:iron")));
        assertNotEquals(JeiRecipeIds.arrow(id("arrowplus:iron")),
                JeiRecipeIds.tipped(id("arrowplus:iron"), id("minecraft:poison")));
    }

    @Test
    void nestedDatapackPathsCannotCrossTheHeadPotionBoundary() {
        assertNotEquals(JeiRecipeIds.tipped(id("a:foo/b/bar"), id("c:baz")),
                JeiRecipeIds.tipped(id("a:foo"), id("b:bar/c/baz")));
        assertEquals(JeiRecipeIds.tipped(id("a:foo"), id("b:bar")),
                JeiRecipeIds.tipped(id("a:foo"), id("b:bar")));
    }
}
