package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArrowPlus.MODID);
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.ENTITY_DATA_SERIALIZERS, ArrowPlus.MODID);

    public static final RegistryObject<EntityType<ModArrowEntity>> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);

     public static final Supplier<EntityDataSerializer<ArrowData>> ARROW_DATA = SERIALIZERS.register("arrow_data", () -> EntityDataSerializer.forValueType(ArrowData.STREAM_CODEC));

    public static RegistryObject<EntityType<ModArrowEntity>> registerArrow(String name, RegistryObject<ModArrowItem> item) {
        return ENTITIES.register(name, () -> EntityType.Builder.<ModArrowEntity>of((entityType, level) ->
                new ModArrowEntity(entityType, level, item.get().getDefaultInstance()), MobCategory.MISC)
                .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, name))));
    }

    public static void register(BusGroup eventBus) {
        ENTITIES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
