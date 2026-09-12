package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getReloadableLookupProvider();

        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModItemTagsProvider(packOutput, lookupProvider));
        event.addProvider(new ModEntityTagsProvider(packOutput, lookupProvider));
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Server event) {
        event.createWorldRegistryObjects(ModDatapackProvider.registrySetBuilder);
        event.createReloadableRegistryObjects(new RegistrySetBuilder().add(new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(BootstrapGetter registries) {
                BootstrapContext<Recipe<?>> recipes = registries.get(Registries.RECIPE);
                BootstrapContext<Advancement> advancements = registries.get(Registries.ADVANCEMENT);
                new ModRecipeProvider(recipes, advancements).buildRecipes();
            }
        }));
    }

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC, builder -> builder.maxId(256));
        event.dataPackRegistry(ModRegistries.FEATHER_DATA_KEY, FeatherData.CODEC, FeatherData.CODEC, builder -> builder.maxId(256));
        event.dataPackRegistry(ModRegistries.STICK_DATA_KEY, StickData.CODEC, StickData.CODEC, builder -> builder.maxId(256));
    }
}
