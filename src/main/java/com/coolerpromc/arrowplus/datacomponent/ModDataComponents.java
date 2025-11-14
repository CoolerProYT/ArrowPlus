package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModDataComponents {
    public static final ComponentType<RegistryEntry<ArrowData>> ARROW_DATA = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ArrowPlus.MODID, "arrow_data"), ComponentType.<RegistryEntry<ArrowData>>builder()
            .codec(RegistryElementCodec.of(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).packetCodec(PacketCodecs.registryEntry(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)).build());

    public static void register(){

    }
}
