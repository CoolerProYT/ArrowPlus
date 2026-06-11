package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class FabricArrowPlus implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ArrowPlus.init();
        DynamicRegistries.registerSynced(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC);
    }
}
