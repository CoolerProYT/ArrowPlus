package com.coolerpromc.arrowplus.registry;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModRegistries {
    public static final ResourceKey<Registry<ArrowData>> ARROW_DATA_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrows"));
    public static final ResourceKey<Registry<FeatherData>> FEATHER_DATA_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "feathers"));
    public static final ResourceKey<Registry<StickData>> STICK_DATA_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "sticks"));
}
