package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ArrowPlus.MODID);

    public static final Supplier<DataComponentType<ArrowData>> ARROW_DATA = DATA_COMPONENTS.registerComponentType("arrow_data", builder -> builder.persistent(ArrowData.CODEC).networkSynchronized(ArrowData.STREAM_CODEC));

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
