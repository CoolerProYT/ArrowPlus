package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ArrowPlus.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);

        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));
    }

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC, builder -> builder.maxId(256));
    }
}
