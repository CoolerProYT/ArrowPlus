package com.coolerpromc.arrowplus.datacomponent;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ArrowPlus.MODID);

    public static final RegistryObject<DataComponentType<Holder<ArrowData>>> ARROW_DATA = DATA_COMPONENTS.register("arrow_data", () -> new DataComponentType.Builder<Holder<ArrowData>>()
            .persistent(RegistryFileCodec.create(ModRegistries.ARROW_DATA_KEY, ArrowData.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.ARROW_DATA_KEY, ArrowData.STREAM_CODEC)).build());

    public static void register(BusGroup eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
