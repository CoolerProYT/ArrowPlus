package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {
    public static final RegistryHandler<EntityType<ModArrowEntity>> ARROW_PLUS = registerArrow("arrow_plus", () -> ModItems.ARROW_PLUS);
    public static final RegistryHandler<EntityDataSerializer<ArrowData>> ARROW_DATA = Services.REGISTRY.registerEntityDataSerializer("arrow_data", ArrowData.STREAM_CODEC);

    public static RegistryHandler<EntityType<ModArrowEntity>> registerArrow(String name, Supplier<RegistryHandler<ModArrowItem>> item) {
        return Services.REGISTRY.registerEntity(name, (entityType, level) -> new ModArrowEntity(entityType, level, item.get().toStack()), MobCategory.MISC, builder -> builder.sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));
    }

    public static void load() {
    }
}
