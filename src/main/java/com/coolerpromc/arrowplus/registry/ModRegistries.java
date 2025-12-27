package com.coolerpromc.arrowplus.registry;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModRegistries {
    public static final ResourceKey<Registry<ArrowData>> ARROW_DATA_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrows"));
}
