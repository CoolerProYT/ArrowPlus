package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializer {
    public static final RecipeSerializer<ArrowRecipe> ARROW_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(ArrowPlus.MODID, "arrow_recipe"), new SpecialRecipeSerializer<>(ArrowRecipe::new));
    public static final RecipeSerializer<TippedArrowRecipe> TIPPED_ARROW_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(ArrowPlus.MODID, "tipped_arrow_recipe"), new SpecialRecipeSerializer<>(TippedArrowRecipe::new));

    public static void register(){

    }
}
