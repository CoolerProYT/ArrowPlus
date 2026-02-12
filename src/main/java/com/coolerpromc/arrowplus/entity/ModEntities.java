package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<ModArrowEntity> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);
    public static final EntityDataSerializer<ArrowData> ARROW_DATA = EntityDataSerializer.forValueType(ArrowData.STREAM_CODEC);

    public static EntityType<ModArrowEntity> registerArrow(String name, ModArrowItem item) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, name), EntityType.Builder.<ModArrowEntity>of((type, world) -> new ModArrowEntity(type, world, item.getDefaultInstance()), MobCategory.MISC)
                .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, name))));
    }

    public static void register() {
        FabricEntityDataRegistry.register(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_data"), ARROW_DATA);
    }
}
