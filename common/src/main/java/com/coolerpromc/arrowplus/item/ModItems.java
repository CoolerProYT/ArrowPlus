package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final RegistryHandler<ModArrowItem> ARROW_PLUS = registerItem("arrow_plus", properties -> new ModArrowItem(properties.component(DataComponents.POTION_DURATION_SCALE, 0.125f), ModEntities.ARROW_PLUS.get()));

    // Sticks
    public static final RegistryHandler<ModStickItem> COPPER_STICK = registerItem("copper_stick", properties -> new ModStickItem(properties, 0xFFD46D44));
    public static final RegistryHandler<ModStickItem> IRON_STICK = registerItem("iron_stick", properties -> new ModStickItem(properties, 0xFFB0BEC5));
    public static final RegistryHandler<ModStickItem> GOLD_STICK = registerItem("gold_stick", properties -> new ModStickItem(properties, 0xFFFFD600));
    public static final RegistryHandler<ModStickItem> DIAMOND_STICK = registerItem("diamond_stick", properties -> new ModStickItem(properties, 0xFF5ee6e6));
    public static final RegistryHandler<ModStickItem> EMERALD_STICK = registerItem("emerald_stick", properties -> new ModStickItem(properties, 0xFF00C853));
    public static final RegistryHandler<ModStickItem> NETHERITE_STICK = registerItem("netherite_stick", properties -> new ModStickItem(properties, 0xFF3E3E3E));

    // Feathers
    public static final RegistryHandler<ModFeatherItem> GILDED_FEATHER = registerItem("gilded_feather", properties -> new ModFeatherItem(properties, 0xFFfffc69));

    private static <T extends Item> RegistryHandler<T> registerItem(String name, Function<Item.Properties, T> item){
        return Services.REGISTRY.registerItem(name, item);
    }

    public static void load() {
    }
}
