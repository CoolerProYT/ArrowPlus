package com.coolerpromc.arrowplus.datapack.arrow;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

public class Arrows {
    public static final ResourceKey<ArrowData> STONE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("stone"));
    public static final ResourceKey<ArrowData> IRON = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("iron"));
    public static final ResourceKey<ArrowData> COPPER = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("copper"));
    public static final ResourceKey<ArrowData> GOLD = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("gold"));
    public static final ResourceKey<ArrowData> LAPIS = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("lapis"));
    public static final ResourceKey<ArrowData> EMERALD = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("emerald"));
    public static final ResourceKey<ArrowData> DIAMOND = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("diamond"));
    public static final ResourceKey<ArrowData> OBSIDIAN = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("obsidian"));
    public static final ResourceKey<ArrowData> AMETHYST = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("amethyst"));
    public static final ResourceKey<ArrowData> NETHERITE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("netherite"));
    public static final ResourceKey<ArrowData> WOOD = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("wood"));
    public static final ResourceKey<ArrowData> BONE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("bone"));
    public static final ResourceKey<ArrowData> BRICK = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("brick"));
    public static final ResourceKey<ArrowData> QUARTZ = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("quartz"));
    public static final ResourceKey<ArrowData> PRISMARINE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("prismarine"));
    public static final ResourceKey<ArrowData> GLOWSTONE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("glowstone"));
    public static final ResourceKey<ArrowData> REDSTONE = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("redstone"));
    public static final ResourceKey<ArrowData> CHARCOAL = ResourceKey.create(ModRegistries.ARROW_DATA_KEY, Constants.id("charcoal"));

    public static void bootstrap(BootstrapContext<ArrowData> context) {
        HolderGetter<FeatherData> featherData = context.lookup(ModRegistries.FEATHER_DATA_KEY);
        HolderGetter<StickData> stickData = context.lookup(ModRegistries.STICK_DATA_KEY);
        HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

        context.register(STONE, ArrowData.builder(Ingredient.of(itemHolderGetter.getOrThrow(ItemTags.STONE_CRAFTING_MATERIALS))).baseDamage(1.5).color(0xFF4D4B49).translationKey("item.arrowplus.stone_arrow").outputAmount(8).build());
        context.register(IRON, ArrowData.builder(Items.IRON_INGOT).baseDamage(2.5).color(0xFFB0BEC5).translationKey("item.arrowplus.iron_arrow").stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.COPPER)).build());
        context.register(COPPER, ArrowData.builder(Items.COPPER_INGOT).baseDamage(2.2).color(0xFFD46D44).translationKey("item.arrowplus.copper_arrow").build());
        context.register(GOLD, ArrowData.builder(Items.GOLD_INGOT).baseDamage(2.0).color(0xFFFFD600).translationKey("item.arrowplus.gold_arrow").stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.IRON)).build());
        context.register(LAPIS, ArrowData.builder(Items.LAPIS_LAZULI).baseDamage(2.3).color(0xFF3F51B5).translationKey("item.arrowplus.lapis_arrow").build());
        context.register(EMERALD, ArrowData.builder(Items.EMERALD).baseDamage(3.0).color(0xFF00C853).translationKey("item.arrowplus.emerald_arrow").stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.DIAMOND)).outputAmount(3).build());
        context.register(DIAMOND, ArrowData.builder(Items.DIAMOND).baseDamage(3.5).color(0xFF40C4FF).translationKey("item.arrowplus.diamond_arrow").stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.GOLD)).outputAmount(3).build());
        context.register(OBSIDIAN, ArrowData.builder(Items.OBSIDIAN).baseDamage(3.8).color(0xFF2E1A47).translationKey("item.arrowplus.obsidian_arrow").feather(ModItems.CUSTOM_FEATHER.get()).featherData(featherData.getOrThrow(Feathers.GILDED)).stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.DIAMOND)).outputAmount(2).build());
        context.register(AMETHYST, ArrowData.builder(Items.AMETHYST_SHARD).baseDamage(3.2).color(0xFF9C27B0).translationKey("item.arrowplus.amethyst_arrow").build());
        context.register(NETHERITE, ArrowData.builder(Items.NETHERITE_INGOT).baseDamage(4.5).color(0xFF3E3E3E).translationKey("item.arrowplus.netherite_arrow").feather(ModItems.CUSTOM_FEATHER.get()).featherData(featherData.getOrThrow(Feathers.GILDED)).stick(ModItems.CUSTOM_STICK.get()).stickData(stickData.getOrThrow(Sticks.DIAMOND)).outputAmount(2).build());
        context.register(WOOD, ArrowData.builder(Ingredient.of(itemHolderGetter.getOrThrow(ItemTags.PLANKS))).baseDamage(1.0).color(0xFF8D6E63).translationKey("item.arrowplus.wood_arrow").outputAmount(8).build());
        context.register(BONE, ArrowData.builder(Items.BONE).baseDamage(2.4).color(0xFFEEE8AA).translationKey("item.arrowplus.bone_arrow").outputAmount(8).build());
        context.register(BRICK, ArrowData.builder(Items.BRICK).baseDamage(2.0).color(0xFFB66A50).translationKey("item.arrowplus.brick_arrow").outputAmount(8).build());
        context.register(QUARTZ, ArrowData.builder(Items.QUARTZ).baseDamage(2.7).color(0xFFF5F5F5).translationKey("item.arrowplus.quartz_arrow").outputAmount(6).build());
        context.register(PRISMARINE, ArrowData.builder(Items.PRISMARINE_SHARD).baseDamage(2.8).color(0xFF5EC8C8).translationKey("item.arrowplus.prismarine_arrow").outputAmount(6).build());
        context.register(GLOWSTONE, ArrowData.builder(Items.GLOWSTONE_DUST).baseDamage(2.0).color(0xFFFFF176).translationKey("item.arrowplus.glowstone_arrow").outputAmount(6).build());
        context.register(REDSTONE, ArrowData.builder(Items.REDSTONE).baseDamage(1.8).color(0xFFFF1744).translationKey("item.arrowplus.redstone_arrow").build());
        context.register(CHARCOAL, ArrowData.builder(Items.CHARCOAL).baseDamage(1.6).color(0xFF3E2723).translationKey("item.arrowplus.charcoal_arrow").outputAmount(6).build());
    }

    public record ArrowRecipeEntry(
            ResourceKey<ArrowData> key,
            Either<Item, TagKey<Item>> material,
            Item stick,
            Optional<ResourceKey<StickData>> stickData,
            Item feather,
            Optional<ResourceKey<FeatherData>> featherData,
            int outputAmount
    ) {
        public ArrowRecipeEntry(
                ResourceKey<ArrowData> key,
                Item material,
                Item stick,
                Optional<ResourceKey<StickData>> stickData,
                Item feather,
                Optional<ResourceKey<FeatherData>> featherData,
                int outputAmount
        ){
            this(key, Either.left(material), stick, stickData, feather, featherData, outputAmount);
        }

        public ArrowRecipeEntry(
                ResourceKey<ArrowData> key,
                TagKey<Item> material,
                Item stick,
                Optional<ResourceKey<StickData>> stickData,
                Item feather,
                Optional<ResourceKey<FeatherData>> featherData,
                int outputAmount
        ){
            this(key, Either.right(material), stick, stickData, feather, featherData, outputAmount);
        }
    }

    private static List<ArrowRecipeEntry> cachedRecipeEntries;

    public static List<ArrowRecipeEntry> getRecipeEntries() {
        if (cachedRecipeEntries == null) {
            cachedRecipeEntries = List.of(
                    new ArrowRecipeEntry(STONE, ItemTags.STONE_CRAFTING_MATERIALS, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 8),
                    new ArrowRecipeEntry(IRON, Items.IRON_INGOT, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.COPPER), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(COPPER, Items.COPPER_INGOT, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(GOLD, Items.GOLD_INGOT, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.IRON), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(LAPIS, Items.LAPIS_LAZULI, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(EMERALD, Items.EMERALD, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.DIAMOND), Items.FEATHER, Optional.empty(), 3),
                    new ArrowRecipeEntry(DIAMOND, Items.DIAMOND, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.GOLD), Items.FEATHER, Optional.empty(), 3),
                    new ArrowRecipeEntry(OBSIDIAN, Items.OBSIDIAN, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.DIAMOND), ModItems.CUSTOM_FEATHER.get(), Optional.of(Feathers.GILDED), 2),
                    new ArrowRecipeEntry(AMETHYST, Items.AMETHYST_SHARD, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(NETHERITE, Items.NETHERITE_INGOT, ModItems.CUSTOM_STICK.get(), Optional.of(Sticks.DIAMOND), ModItems.CUSTOM_FEATHER.get(), Optional.of(Feathers.GILDED), 2),
                    new ArrowRecipeEntry(WOOD, ItemTags.PLANKS, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 8),
                    new ArrowRecipeEntry(BONE, Items.BONE, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 8),
                    new ArrowRecipeEntry(BRICK, Items.BRICK, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 8),
                    new ArrowRecipeEntry(QUARTZ, Items.QUARTZ, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 6),
                    new ArrowRecipeEntry(PRISMARINE, Items.PRISMARINE_SHARD, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 6),
                    new ArrowRecipeEntry(GLOWSTONE, Items.GLOWSTONE_DUST, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 6),
                    new ArrowRecipeEntry(REDSTONE, Items.REDSTONE, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 4),
                    new ArrowRecipeEntry(CHARCOAL, Items.CHARCOAL, Items.STICK, Optional.empty(), Items.FEATHER, Optional.empty(), 6)
            );
        }
        return cachedRecipeEntries;
    }
}