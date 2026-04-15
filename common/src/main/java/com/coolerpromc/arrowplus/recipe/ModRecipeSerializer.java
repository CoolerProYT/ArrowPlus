package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializer {
    public static final RegistryHandler<RecipeSerializer<ArrowRecipe>> ARROW_RECIPE_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("arrow_recipe", ArrowRecipe.SERIALIZER);
    public static final RegistryHandler<RecipeSerializer<TippedArrowRecipe>> TIPPED_ARROW_RECIPE_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("tipped_arrow_recipe", TippedArrowRecipe.SERIALIZER);

    public static void load(){
    }
}
