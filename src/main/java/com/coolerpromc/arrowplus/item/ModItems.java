package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final ModArrowItem ARROW_PLUS = registerItem("arrow_plus", properties -> new ModArrowItem(properties, ModEntities.ARROW_PLUS));

    private static ModArrowItem registerItem(String name, Function<Item.Settings, ModArrowItem> item){
        return Registry.register(Registries.ITEM, Identifier.of(ArrowPlus.MODID, name), item.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ArrowPlus.MODID, name)))));
    }

    public static void register() {
    }
}
