package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.server.MinecraftServer;

public class FabricArrowPlus implements ModInitializer {
    public static MinecraftServer server = null;

    @Override
    public void onInitialize() {
        ArrowPlus.init();
        DynamicRegistries.registerSynced(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC, ArrowData.CODEC);
        DynamicRegistries.registerSynced(ModRegistries.FEATHER_DATA_KEY, FeatherData.CODEC, FeatherData.CODEC);
        DynamicRegistries.registerSynced(ModRegistries.STICK_DATA_KEY, StickData.CODEC, StickData.CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(s -> server = s);
        ServerLifecycleEvents.SERVER_STOPPED.register(_ -> server = null);
    }
}
