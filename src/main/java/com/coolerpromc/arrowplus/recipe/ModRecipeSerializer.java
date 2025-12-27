package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ArrowPlus.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArrowRecipe>> ARROW_RECIPE_SERIALIZER = SERIALIZERS.register("arrow_recipe", () -> new CustomRecipe.Serializer<>(ArrowRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TippedArrowRecipe>> TIPPED_ARROW_RECIPE_SERIALIZER = SERIALIZERS.register("tipped_arrow_recipe", () -> new CustomRecipe.Serializer<>(TippedArrowRecipe::new));

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
