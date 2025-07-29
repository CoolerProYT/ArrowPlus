package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArrowPlus.MODID);

    public static final DeferredItem<ModArrowItem> ARROW_PLUS = registerItem("arrow_plus", properties -> new ModArrowItem(properties, ModEntities.ARROW_PLUS.get()));

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, ? extends T> item){
        return ITEMS.registerItem(name, item);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
