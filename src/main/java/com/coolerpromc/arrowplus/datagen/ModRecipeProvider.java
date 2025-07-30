package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SpecialRecipeBuilder.special(ArrowRecipe::new).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_recipe"));
    }
}
