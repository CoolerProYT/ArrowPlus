package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ArrowPlus implements ModInitializer {
	public static final String MODID = "arrowplus";

	@Override
	public void onInitialize() {
		ModEntities.register();
		ModItems.register();
		ModRecipeSerializer.register();
		ModCreativeTabs.register();
		ModDataComponents.register();

		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("arrowplus-common.toml");
		ArrowPlusConfig.load(configPath);
		DynamicRegistries.registerSynced(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC);
	}
}