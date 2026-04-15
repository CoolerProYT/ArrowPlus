package com.coolerpromc.arrowplus.registry;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
    public static final ResourceKey<Registry<ArrowData>> ARROW_DATA_KEY = ResourceKey.createRegistryKey(Constants.id("arrows"));
}
