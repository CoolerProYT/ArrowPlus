package com.coolerpromc.arrowplus.arrow;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.item.Items;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.Map;

public class Arrows {
    public static final RegistryKey<ArrowData> STONE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "stone"));
    public static final RegistryKey<ArrowData> IRON = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "iron"));
    public static final RegistryKey<ArrowData> COPPER = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "copper"));
    public static final RegistryKey<ArrowData> GOLD = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "gold"));
    public static final RegistryKey<ArrowData> LAPIS = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "lapis"));
    public static final RegistryKey<ArrowData> EMERALD = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "emerald"));
    public static final RegistryKey<ArrowData> DIAMOND = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "diamond"));
    public static final RegistryKey<ArrowData> OBSIDIAN = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "obsidian"));
    public static final RegistryKey<ArrowData> AMETHYST = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "amethyst"));
    public static final RegistryKey<ArrowData> NETHERITE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "netherite"));
    public static final RegistryKey<ArrowData> WOOD = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "wood"));
    public static final RegistryKey<ArrowData> BONE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "bone"));
    public static final RegistryKey<ArrowData> BRICK = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "brick"));
    public static final RegistryKey<ArrowData> QUARTZ = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "quartz"));
    public static final RegistryKey<ArrowData> PRISMARINE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "prismarine"));
    public static final RegistryKey<ArrowData> GLOWSTONE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "glowstone"));
    public static final RegistryKey<ArrowData> REDSTONE = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "redstone"));
    public static final RegistryKey<ArrowData> CHARCOAL = RegistryKey.of(ModRegistries.ARROW_DATA_KEY, Identifier.of(ArrowPlus.MODID, "charcoal"));

    public static void bootstrap(Registerable<ArrowData> context){
        context.register(STONE, new ArrowData(ItemTags.STONE_CRAFTING_MATERIALS, 1.5d, 0xFF4D4B49, "item.arrowplus.stone_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 8));
        context.register(IRON, new ArrowData(Items.IRON_INGOT, 2.5d, 0xFFB0BEC5, "item.arrowplus.iron_arrow", false, 0.05, Map.of(), Items.FEATHER, ModItems.COPPER_STICK, 4));
        context.register(COPPER, new ArrowData(Items.COPPER_INGOT, 2.2d, 0xFFD46D44, "item.arrowplus.copper_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 4));
        context.register(GOLD, new ArrowData(Items.GOLD_INGOT, 2.0d, 0xFFFFD600, "item.arrowplus.gold_arrow", false, 0.05, Map.of(), Items.FEATHER, ModItems.IRON_STICK, 4));
        context.register(LAPIS, new ArrowData(Items.LAPIS_LAZULI, 2.3d, 0xFF3F51B5, "item.arrowplus.lapis_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 4));
        context.register(EMERALD, new ArrowData(Items.EMERALD, 3.0d, 0xFF00C853, "item.arrowplus.emerald_arrow", false, 0.05, Map.of(), Items.FEATHER, ModItems.DIAMOND_STICK, 3));
        context.register(DIAMOND, new ArrowData(Items.DIAMOND, 3.5d, 0xFF40C4FF, "item.arrowplus.diamond_arrow", false, 0.05, Map.of(), Items.FEATHER, ModItems.GOLD_STICK, 3));
        context.register(OBSIDIAN, new ArrowData(Items.OBSIDIAN, 3.8d, 0xFF2E1A47, "item.arrowplus.obsidian_arrow", false, 0.05, Map.of(), ModItems.GILDED_FEATHER, ModItems.DIAMOND_STICK, 2));
        context.register(AMETHYST, new ArrowData(Items.AMETHYST_SHARD, 3.2d, 0xFF9C27B0, "item.arrowplus.amethyst_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 4));
        context.register(NETHERITE, new ArrowData(Items.NETHERITE_INGOT, 4.5d, 0xFF3E3E3E, "item.arrowplus.netherite_arrow", false, 0.05, Map.of(), ModItems.GILDED_FEATHER, ModItems.DIAMOND_STICK, 2));
        context.register(WOOD, new ArrowData(ItemTags.PLANKS, 1.0d, 0xFF8D6E63, "item.arrowplus.wood_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 8));
        context.register(BONE, new ArrowData(Items.BONE, 2.4d, 0xFFEEE8AA, "item.arrowplus.bone_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 8));
        context.register(BRICK, new ArrowData(Items.BRICK, 2.0d, 0xFFB66A50, "item.arrowplus.brick_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 8));
        context.register(QUARTZ, new ArrowData(Items.QUARTZ, 2.7d, 0xFFF5F5F5, "item.arrowplus.quartz_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 6));
        context.register(PRISMARINE, new ArrowData(Items.PRISMARINE_SHARD, 2.8d, 0xFF5EC8C8, "item.arrowplus.prismarine_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 6));
        context.register(GLOWSTONE, new ArrowData(Items.GLOWSTONE_DUST, 2.0d, 0xFFFFF176, "item.arrowplus.glowstone_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 6));
        context.register(REDSTONE, new ArrowData(Items.REDSTONE, 1.8d, 0xFFFF1744, "item.arrowplus.redstone_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 4));
        context.register(CHARCOAL, new ArrowData(Items.CHARCOAL, 1.6d, 0xFF3E2723, "item.arrowplus.charcoal_arrow", false, 0.05, Map.of(), Items.FEATHER, Items.STICK, 6));
    }
}
