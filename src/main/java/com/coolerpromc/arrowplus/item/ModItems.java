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
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> COPPER_STICK = registerItem("copper_stick", ModStickItem::new);
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> IRON_STICK = registerItem("iron_stick", ModStickItem::new);
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> GOLD_STICK = registerItem("gold_stick", ModStickItem::new);
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> DIAMOND_STICK = registerItem("diamond_stick", ModStickItem::new);
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> EMERALD_STICK = registerItem("emerald_stick", ModStickItem::new);
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModStickItem> NETHERITE_STICK = registerItem("netherite_stick", ModStickItem::new);
    public static final DeferredItem<ModStickItem> CUSTOM_STICK = registerItem("custom_stick", ModStickItem::new);

    // Feathers
    @Deprecated(forRemoval = true)
    public static final DeferredItem<ModFeatherItem> GILDED_FEATHER = registerItem("gilded_feather", ModFeatherItem::new);
    public static final DeferredItem<ModFeatherItem> CUSTOM_FEATHER = registerItem("custom_feather", ModFeatherItem::new);

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, ? extends T> item){
        return ITEMS.registerItem(name, item);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
