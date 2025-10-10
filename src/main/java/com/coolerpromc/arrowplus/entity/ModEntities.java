package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.core.registries.Registries;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ArrowPlus.MODID);
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, ArrowPlus.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ModArrowEntity>> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);

    public static final Supplier<EntityDataSerializer<ArrowData>> ARROW_DATA = SERIALIZERS.register("arrow_data", () -> EntityDataSerializer.forValueType(ArrowData.STREAM_CODEC));

    public static DeferredHolder<EntityType<?>, EntityType<ModArrowEntity>> registerArrow(String name, DeferredItem<ModArrowItem> item) {
        return ENTITIES.register(name, () -> EntityType.Builder.<ModArrowEntity>of(
                (entityType, level) -> new ModArrowEntity(entityType, level, item.toStack(1)), MobCategory.MISC
        ).sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build(name));
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
