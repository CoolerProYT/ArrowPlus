package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datagen.ModDatapackProvider;
import com.coolerpromc.arrowplus.datagen.ModItemTagsProvider;
import com.coolerpromc.arrowplus.datagen.ModModelProvider;
import com.coolerpromc.arrowplus.datagen.ModRecipeProvider;
import com.coolerpromc.arrowplus.arrow.Arrows;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import java.util.concurrent.CompletableFuture;

public class ArrowDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		CompletableFuture<HolderLookup.Provider> registriesFuture = fabricDataGenerator.getRegistries();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModDatapackProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(ModRegistries.ARROW_DATA_KEY, Arrows::bootstrap);
	}
}
