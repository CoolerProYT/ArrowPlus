package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(ArrowPlus.MODID);
    public static final Map<String, DeferredHolder<EntityType<?>, EntityType<ModArrowEntity>>> ARROWS = new HashMap<>();

    public static final DeferredHolder<EntityType<?>, EntityType<ModArrowEntity>> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);

    public static DeferredHolder<EntityType<?>, EntityType<ModArrowEntity>> registerArrow(String name, DeferredItem<ModArrowItem> item) {
        return ENTITIES.registerEntityType(name, (entityType, level) -> new ModArrowEntity(entityType, level, item.toStack(1)), MobCategory.MISC, builder -> builder.sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
