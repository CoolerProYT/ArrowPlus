package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ArrowPlus.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ModDatapackProvider datapackProvider = new ModDatapackProvider(packOutput, lookupProvider);

        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModItemTagsProvider(packOutput, lookupProvider));
        event.addProvider(new ModRecipeProvider.Runner(packOutput, datapackProvider.getRegistryProvider()));
        event.addProvider(datapackProvider);
    }

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC, builder -> builder.maxId(256));
    }
}
