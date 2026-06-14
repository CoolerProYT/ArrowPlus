package com.coolerpromc.arrowplus.registry;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
    public static final ResourceKey<Registry<ArrowData>> ARROW_DATA_KEY = ResourceKey.createRegistryKey(Constants.id("arrows"));
    public static final ResourceKey<Registry<FeatherData>> FEATHER_DATA_KEY = ResourceKey.createRegistryKey(Constants.id("feathers"));
    public static final ResourceKey<Registry<StickData>> STICK_DATA_KEY = ResourceKey.createRegistryKey(Constants.id("sticks"));
}
