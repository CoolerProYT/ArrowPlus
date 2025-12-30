package com.coolerpromc.arrowplus.registry;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistries {
    public static final RegistryKey<Registry<ArrowData>> ARROW_DATA_KEY = RegistryKey.ofRegistry(Identifier.of(ArrowPlus.MODID, "arrows"));
}
