package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ModArrowEntity> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);

    public static EntityType<ModArrowEntity> registerArrow(String name, ModArrowItem item) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ArrowPlus.MODID, name), EntityType.Builder.<ModArrowEntity>create((type, world) -> new ModArrowEntity(type, world, item.getDefaultStack()), SpawnGroup.MISC)
                .dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build(name));
    }

    public static void register() {
    }
}
