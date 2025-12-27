package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArrowPlus.MODID);

    public static final DeferredItem<ModArrowItem> ARROW_PLUS = registerItem("arrow_plus", properties -> new ModArrowItem(properties, ModEntities.ARROW_PLUS.get()));

    // Sticks
    public static final DeferredItem<ModStickItem> COPPER_STICK = registerItem("copper_stick", properties -> new ModStickItem(properties, 0xFFD46D44));
    public static final DeferredItem<ModStickItem> IRON_STICK = registerItem("iron_stick", properties -> new ModStickItem(properties, 0xFFB0BEC5));
    public static final DeferredItem<ModStickItem> GOLD_STICK = registerItem("gold_stick", properties -> new ModStickItem(properties, 0xFFFFD600));
    public static final DeferredItem<ModStickItem> DIAMOND_STICK = registerItem("diamond_stick", properties -> new ModStickItem(properties, 0xFF5ee6e6));
    public static final DeferredItem<ModStickItem> EMERALD_STICK = registerItem("emerald_stick", properties -> new ModStickItem(properties, 0xFF00C853));
    public static final DeferredItem<ModStickItem> NETHERITE_STICK = registerItem("netherite_stick", properties -> new ModStickItem(properties, 0xFF3E3E3E));

    // Feathers
    public static final DeferredItem<ModFeatherItem> GILDED_FEATHER = registerItem("gilded_feather", properties -> new ModFeatherItem(properties, 0xFFfffc69));

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, ? extends T> item){
        return ITEMS.registerItem(name, item);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
