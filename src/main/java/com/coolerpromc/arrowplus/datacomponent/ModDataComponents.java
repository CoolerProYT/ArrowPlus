package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDataComponents {
    public static final ComponentType<ArrowData> ARROW_DATA = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ArrowPlus.MODID, "arrow_data"), ComponentType.<ArrowData>builder().codec(ArrowData.CODEC).packetCodec(ArrowData.STREAM_CODEC).build());

    public static void register(){

    }
}
