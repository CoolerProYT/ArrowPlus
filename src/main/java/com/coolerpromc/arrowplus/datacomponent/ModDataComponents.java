package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import com.coolerpromc.arrowplus.arrow.ArrowData;

public class ModDataComponents {
    public static final DataComponentType<Holder<ArrowData>> ARROW_DATA = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_data"), DataComponentType.<Holder<ArrowData>>builder()
            .persistent(RegistryFileCodec.create(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)).build());

    public static void register(){

    }
}
