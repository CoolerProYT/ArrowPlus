package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;

public class ModDataComponents {
    public static final RegistryHandler<DataComponentType<Holder<ArrowData>>> ARROW_DATA = Services.REGISTRY.registerDataComponent("arrow_data",builder -> builder.persistent(RegistryFileCodec.create(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)));

    public static void load(){
    }
}
