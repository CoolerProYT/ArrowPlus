package com.coolerpromc.arrowplus.datapack.stick;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Sticks {
    public static final ResourceKey<StickData> STICK = key("stick");
    public static final ResourceKey<StickData> BAMBOO = key("bamboo");
    public static final ResourceKey<StickData> BONE = key("bone");
    public static final ResourceKey<StickData> BLAZE_ROD = key("blaze_rod");
    public static final ResourceKey<StickData> BREEZE_ROD = key("breeze_rod");
    public static final ResourceKey<StickData> COPPER = key("copper");
    public static final ResourceKey<StickData> IRON = key("iron");
    public static final ResourceKey<StickData> GOLD = key("gold");
    public static final ResourceKey<StickData> DIAMOND = key("diamond");
    public static final ResourceKey<StickData> EMERALD = key("emerald");
    public static final ResourceKey<StickData> NETHERITE = key("netherite");

    public static final TagKey<Item> WOODEN_RODS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "rods/wooden"));

    public static void bootstrap(BootstrapContext<StickData> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        context.register(STICK, StickData.standalone(items.getOrThrow(WOODEN_RODS), StickData.VANILLA_COLOR, "item.minecraft.stick", 0.05, 1.0));
        context.register(BAMBOO, StickData.standalone(Items.BAMBOO, 0xFF7FA332, "block.minecraft.bamboo", 0.045, 0.9));
        context.register(BONE, StickData.standalone(Items.BONE, 0xFFE8E4CF, "item.minecraft.bone", 0.05, 1.05));
        context.register(BLAZE_ROD, StickData.standalone(Items.BLAZE_ROD, 0xFFF5B400, "item.minecraft.blaze_rod", 0.06, 1.2));
        context.register(BREEZE_ROD, StickData.standalone(Items.BREEZE_ROD, 0xFFA9B5E6, "item.minecraft.breeze_rod", 0.03, 1.1));

        context.register(COPPER, new StickData(Items.COPPER_INGOT, 0xFFD46D44, "item.arrowplus.copper_stick", 4, 0.05, 1.1));
        context.register(IRON, new StickData(Items.IRON_INGOT, 0xFFB0BEC5, "item.arrowplus.iron_stick", 4, 0.055, 1.2));
        context.register(GOLD, new StickData(Items.GOLD_INGOT, 0xFFFFD600, "item.arrowplus.gold_stick", 4, 0.05, 1.15));
        context.register(DIAMOND, new StickData(Items.DIAMOND, 0xFF5ee6e6, "item.arrowplus.diamond_stick", 4, 0.045, 1.1));
        context.register(EMERALD, new StickData(Items.EMERALD, 0xFF00C853, "item.arrowplus.emerald_stick", 4, 0.035, 1.0));
        context.register(NETHERITE, new StickData(Items.NETHERITE_INGOT, 0xFF3E3E3E, "item.arrowplus.netherite_stick", 4, 0.12, 1.25));
    }

    private static ResourceKey<StickData> key(String name){
        return ResourceKey.create(ModRegistries.STICK_DATA_KEY, Constants.id(name));
    }
}
