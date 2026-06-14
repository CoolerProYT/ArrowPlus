package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;

public class ModDataComponents {
    public static final RegistryHandler<DataComponentType<Holder<ArrowData>>> ARROW_DATA = Services.REGISTRY.registerDataComponent("arrow_data",builder -> builder.persistent(RegistryFileCodec.create(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)));
    public static final RegistryHandler<DataComponentType<Holder<FeatherData>>> FEATHER_DATA = Services.REGISTRY.registerDataComponent("feather_data", builder -> builder.persistent(RegistryFileCodec.create(ModRegistries.FEATHER_DATA_KEY, FeatherData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.FEATHER_DATA_KEY, FeatherData.STREAM_CODEC)));
    public static final RegistryHandler<DataComponentType<Holder<StickData>>> STICK_DATA = Services.REGISTRY.registerDataComponent("stick_data", builder -> builder.persistent(RegistryFileCodec.create(ModRegistries.STICK_DATA_KEY, StickData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.STICK_DATA_KEY, StickData.STREAM_CODEC)));

    public static void load(){
    }
}
