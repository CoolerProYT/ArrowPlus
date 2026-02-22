package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.arrow.Arrows;
import com.coolerpromc.arrowplus.datagen.ModDatapackProvider;
import com.coolerpromc.arrowplus.datagen.ModItemTagsProvider;
import com.coolerpromc.arrowplus.datagen.ModModelProvider;
import com.coolerpromc.arrowplus.datagen.ModRecipeProvider;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;

public class ArrowDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		if (!(fabricDataGenerator.getModId().equals(ArrowPlus.MODID))) return;

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModDatapackProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(ModRegistries.ARROW_DATA_KEY, Arrows::bootstrap);
	}
}
