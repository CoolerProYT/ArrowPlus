package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModItemTagsProvider(packOutput, lookupProvider));
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Server event) {
        event.createDatapackRegistryObjects(ModDatapackProvider.registrySetBuilder);
        event.createProvider(ModRecipeProvider.Runner::new);
    }

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC, builder -> builder.maxId(256));
        event.dataPackRegistry(ModRegistries.FEATHER_DATA_KEY, FeatherData.CODEC, FeatherData.CODEC, builder -> builder.maxId(256));
        event.dataPackRegistry(ModRegistries.STICK_DATA_KEY, StickData.CODEC, StickData.CODEC, builder -> builder.maxId(256));
    }
}
