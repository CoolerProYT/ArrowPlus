package com.coolerpromc.arrowplus.datapack.feather;

import com.coolerpromc.arrowplus.datapack.ArrowPhysics;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public record FeatherData(HolderSet<Item> material, int color, String translationKey, int outputAmount, double airDrag, double waterDrag, double accuracy, boolean standalone, double gravityMultiplier) {
    public static final double DEFAULT_AIR_DRAG = 0.99;
    public static final double DEFAULT_WATER_DRAG = 0.6;
    public static final double DEFAULT_ACCURACY = 1.0;

    public static final Codec<FeatherData> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("material").forGetter(FeatherData::material),
            Codec.INT.fieldOf("color").forGetter(FeatherData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(FeatherData::translationKey),
            Codec.intRange(1, 64).optionalFieldOf("outputAmount", 4).forGetter(FeatherData::outputAmount),
            ArrowPhysics.DRAG.optionalFieldOf("airDrag", DEFAULT_AIR_DRAG).forGetter(FeatherData::airDrag),
            ArrowPhysics.DRAG.optionalFieldOf("waterDrag", DEFAULT_WATER_DRAG).forGetter(FeatherData::waterDrag),
            ArrowPhysics.ACCURACY.optionalFieldOf("accuracy", DEFAULT_ACCURACY).forGetter(FeatherData::accuracy),
            Codec.BOOL.optionalFieldOf("standalone", false).forGetter(FeatherData::standalone),
            ArrowPhysics.GRAVITY_MULTIPLIER.optionalFieldOf("gravityMultiplier", 1.0).forGetter(FeatherData::gravityMultiplier)
    ).apply(i, FeatherData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FeatherData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM),
            FeatherData::material,
            ByteBufCodecs.INT,
            FeatherData::color,
            ByteBufCodecs.STRING_UTF8,
            FeatherData::translationKey,
            ByteBufCodecs.INT,
            FeatherData::outputAmount,
            ByteBufCodecs.DOUBLE,
            FeatherData::airDrag,
            ByteBufCodecs.DOUBLE,
            FeatherData::waterDrag,
            ByteBufCodecs.DOUBLE,
            FeatherData::accuracy,
            ByteBufCodecs.BOOL,
            FeatherData::standalone,
            ByteBufCodecs.DOUBLE,
            FeatherData::gravityMultiplier,
            FeatherData::new
    );
    public static final FeatherData EMPTY = new FeatherData(Items.BEDROCK, -1, "item.arrowplus.cheated_item");

    public static final FeatherData VANILLA = standalone(Items.FEATHER, -1, "item.minecraft.feather", DEFAULT_AIR_DRAG, DEFAULT_WATER_DRAG, DEFAULT_ACCURACY);

    public FeatherData(ItemLike material, int color, String translationKey){
        this(material, color, translationKey, 4, DEFAULT_AIR_DRAG, DEFAULT_WATER_DRAG, DEFAULT_ACCURACY);
    }

    public FeatherData(TagKey<Item> material, int color, String translationKey){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, 4, DEFAULT_AIR_DRAG, DEFAULT_WATER_DRAG, DEFAULT_ACCURACY, false);
    }

    public FeatherData(ItemLike material, int color, String translationKey, int outputAmount){
        this(material, color, translationKey, outputAmount, DEFAULT_AIR_DRAG, DEFAULT_WATER_DRAG, DEFAULT_ACCURACY);
    }

    public FeatherData(TagKey<Item> material, int color, String translationKey, int outputAmount){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, outputAmount, DEFAULT_AIR_DRAG, DEFAULT_WATER_DRAG, DEFAULT_ACCURACY, false);
    }

    public FeatherData(ItemLike material, int color, String translationKey, int outputAmount, double airDrag, double waterDrag, double accuracy){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, outputAmount, airDrag, waterDrag, accuracy, false);
    }

    public static FeatherData standalone(ItemLike material, int color, String translationKey, double airDrag, double waterDrag, double accuracy){
        return new FeatherData(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, 1, airDrag, waterDrag, accuracy, true);
    }

    public static FeatherData standalone(HolderSet<Item> material, int color, String translationKey, double airDrag, double waterDrag, double accuracy){
        return new FeatherData(material, color, translationKey, 1, airDrag, waterDrag, accuracy, true);
    }

    public FeatherData(HolderSet<Item> material, int color, String translationKey, int outputAmount, double airDrag, double waterDrag, double accuracy, boolean standalone) {
        this(material, color, translationKey, outputAmount, airDrag, waterDrag, accuracy, standalone, 1.0);
    }

    public Ingredient ingredient(){
        return Ingredient.of(material);
    }
}
