package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ArrowPlus.MODID);

    public static final Supplier<DataComponentType<Holder<ArrowData>>> ARROW_DATA = DATA_COMPONENTS.registerComponentType("arrow_data", builder ->
            builder.persistent(RegistryFileCodec.create(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)));

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
