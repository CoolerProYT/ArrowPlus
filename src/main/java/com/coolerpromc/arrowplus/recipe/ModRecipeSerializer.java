package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ArrowPlus.MODID);

    public static final RegistryObject<RecipeSerializer<ArrowRecipe>> ARROW_RECIPE_SERIALIZER = SERIALIZERS.register("arrow_recipe", () -> new CustomRecipe.Serializer<>(ArrowRecipe::new));
    public static final RegistryObject<RecipeSerializer<TippedArrowRecipe>> TIPPED_ARROW_RECIPE_SERIALIZER = SERIALIZERS.register("tipped_arrow_recipe", () -> new CustomRecipe.Serializer<>(TippedArrowRecipe::new));

    public static void register(BusGroup eventBus){
        SERIALIZERS.register(eventBus);
    }
}
