package com.coolerpromc.arrowplus.entity;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArrowPlus.MODID);

    public static final RegistryObject<EntityType<ModArrowEntity>> ARROW_PLUS = registerArrow("arrow_plus", ModItems.ARROW_PLUS);

    public static RegistryObject<EntityType<ModArrowEntity>> registerArrow(String name, RegistryObject<ModArrowItem> item) {
        return ENTITIES.register(name, () -> EntityType.Builder.<ModArrowEntity>of((entityType, level) ->
                new ModArrowEntity(entityType, level, item.get().getDefaultInstance()), MobCategory.MISC)
                .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build(name));
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
