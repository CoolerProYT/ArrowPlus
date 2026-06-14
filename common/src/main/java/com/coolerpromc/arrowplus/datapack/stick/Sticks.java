package com.coolerpromc.arrowplus.datapack.stick;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

public class Sticks {
    public static final ResourceKey<StickData> COPPER = key("copper");
    public static final ResourceKey<StickData> IRON = key("iron");
    public static final ResourceKey<StickData> GOLD = key("gold");
    public static final ResourceKey<StickData> DIAMOND = key("diamond");
    public static final ResourceKey<StickData> EMERALD = key("emerald");
    public static final ResourceKey<StickData> NETHERITE = key("netherite");

    public static void bootstrap(BootstrapContext<StickData> context) {
        context.register(COPPER, new StickData(Items.COPPER_INGOT, 0xFFD46D44, "item.arrowplus.copper_stick"));
        context.register(IRON, new StickData(Items.IRON_INGOT, 0xFFB0BEC5, "item.arrowplus.iron_stick"));
        context.register(GOLD, new StickData(Items.GOLD_INGOT, 0xFFFFD600, "item.arrowplus.gold_stick"));
        context.register(DIAMOND, new StickData(Items.DIAMOND, 0xFF5ee6e6, "item.arrowplus.diamond_stick"));
        context.register(EMERALD, new StickData(Items.EMERALD, 0xFF00C853, "item.arrowplus.emerald_stick"));
        context.register(NETHERITE, new StickData(Items.NETHERITE_INGOT, 0xFF3E3E3E, "item.arrowplus.netherite_stick"));
    }

    private static ResourceKey<StickData> key(String name){
        return ResourceKey.create(ModRegistries.STICK_DATA_KEY, Constants.id(name));
    }
}