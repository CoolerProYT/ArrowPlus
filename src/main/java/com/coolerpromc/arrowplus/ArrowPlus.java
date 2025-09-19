package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class ArrowPlus implements ModInitializer {
	public static final String MODID = "arrowplus";

	@Override
	public void onInitialize() {
		ModEntities.register();
		ModItems.register();
		ModRecipeSerializer.register();
		ModCreativeTabs.register();
		ModDataComponents.register();

		DynamicRegistries.registerSynced(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC);
	}
}