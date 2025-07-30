package com.coolerpromc.arrowplus.util;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ArrowPlus.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArrowRecipe>> ARROW_RECIPE_SERIALIZER = SERIALIZERS.register("arrow_recipe", () -> new SimpleCraftingRecipeSerializer<>(ArrowRecipe::new));

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
