package com.coolerpromc.arrowplus.util;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializer {
    public static final RecipeSerializer<ArrowRecipe> ARROW_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(ArrowPlus.MODID, "arrow_recipe"), new SpecialCraftingRecipe.SpecialRecipeSerializer<>(ArrowRecipe::new));

    public static void register(){

    }
}
