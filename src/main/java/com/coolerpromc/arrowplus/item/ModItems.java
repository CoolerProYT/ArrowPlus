package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final ModArrowItem ARROW_PLUS = registerItem("arrow_plus", properties -> new ModArrowItem(properties.component(DataComponents.POTION_DURATION_SCALE, 0.125f), ModEntities.ARROW_PLUS));

    // Sticks
    public static final ModStickItem COPPER_STICK = registerItem("copper_stick", properties -> new ModStickItem(properties, 0xFFD46D44));
    public static final ModStickItem IRON_STICK = registerItem("iron_stick", properties -> new ModStickItem(properties, 0xFFB0BEC5));
    public static final ModStickItem GOLD_STICK = registerItem("gold_stick", properties -> new ModStickItem(properties, 0xFFFFD600));
    public static final ModStickItem DIAMOND_STICK = registerItem("diamond_stick", properties -> new ModStickItem(properties, 0xFF5ee6e6));
    public static final ModStickItem EMERALD_STICK = registerItem("emerald_stick", properties -> new ModStickItem(properties, 0xFF00C853));
    public static final ModStickItem NETHERITE_STICK = registerItem("netherite_stick", properties -> new ModStickItem(properties, 0xFF3E3E3E));

    // Feathers
    public static final ModFeatherItem GILDED_FEATHER = registerItem("gilded_feather", properties -> new ModFeatherItem(properties, 0xFFfffc69));

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> item){
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, name), item.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, name)))));
    }

    public static void register() {
    }
}
