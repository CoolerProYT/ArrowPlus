package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.FabricArrowPlusConfig;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ArrowPlus implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("arrowplus-common.toml");
        FabricArrowPlusConfig.load(configPath);
        DynamicRegistries.registerSynced(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC);
    }
}
