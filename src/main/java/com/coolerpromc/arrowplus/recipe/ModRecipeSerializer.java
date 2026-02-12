package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializer {
    public static final RecipeSerializer<ArrowRecipe> ARROW_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_recipe"), ArrowRecipe.SERIALIZER);
    public static final RecipeSerializer<TippedArrowRecipe> TIPPED_ARROW_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "tipped_arrow_recipe"), TippedArrowRecipe.SERIALIZER);

    public static void register(){

    }
}
