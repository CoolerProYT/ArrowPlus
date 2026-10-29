package com.coolerpromc.arrowplus.datapack.stick;

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

public record StickData(HolderSet<Item> material, int color, String translationKey, int outputAmount, double gravity, double velocity, boolean standalone) {
    public static final double DEFAULT_GRAVITY = 0.05;
    public static final double DEFAULT_VELOCITY = 1.0;
    public static final int VANILLA_COLOR = 0xFF886627;

    public static final Codec<StickData> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("material").forGetter(StickData::material),
            Codec.INT.fieldOf("color").forGetter(StickData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(StickData::translationKey),
            Codec.intRange(1, 64).optionalFieldOf("outputAmount", 4).forGetter(StickData::outputAmount),
            ArrowPhysics.GRAVITY.optionalFieldOf("gravity", DEFAULT_GRAVITY).forGetter(StickData::gravity),
            ArrowPhysics.VELOCITY.optionalFieldOf("velocity", DEFAULT_VELOCITY).forGetter(StickData::velocity),
            Codec.BOOL.optionalFieldOf("standalone", false).forGetter(StickData::standalone)
    ).apply(i, StickData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StickData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM),
            StickData::material,
            ByteBufCodecs.INT,
            StickData::color,
            ByteBufCodecs.STRING_UTF8,
            StickData::translationKey,
            ByteBufCodecs.INT,
            StickData::outputAmount,
            ByteBufCodecs.DOUBLE,
            StickData::gravity,
            ByteBufCodecs.DOUBLE,
            StickData::velocity,
            ByteBufCodecs.BOOL,
            StickData::standalone,
            StickData::new
    );

    public static final StickData EMPTY = new StickData(Items.BEDROCK, -1, "item.arrowplus.cheated_item");

    public static final StickData VANILLA = standalone(Items.STICK, VANILLA_COLOR, "item.minecraft.stick", DEFAULT_GRAVITY, DEFAULT_VELOCITY);

    public StickData(ItemLike material, int color, String translationKey){
        this(material, color, translationKey, 4, DEFAULT_GRAVITY, DEFAULT_VELOCITY);
    }

    public StickData(TagKey<Item> material, int color, String translationKey){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, 4, DEFAULT_GRAVITY, DEFAULT_VELOCITY, false);
    }

    public StickData(ItemLike material, int color, String translationKey, int outputAmount){
        this(material, color, translationKey, outputAmount, DEFAULT_GRAVITY, DEFAULT_VELOCITY);
    }

    public StickData(TagKey<Item> material, int color, String translationKey, int outputAmount){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, outputAmount, DEFAULT_GRAVITY, DEFAULT_VELOCITY, false);
    }

    public StickData(ItemLike material, int color, String translationKey, int outputAmount, double gravity, double velocity){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, outputAmount, gravity, velocity, false);
    }

    public static StickData standalone(ItemLike material, int color, String translationKey, double gravity, double velocity){
        return new StickData(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, 1, gravity, velocity, true);
    }

    public static StickData standalone(HolderSet<Item> material, int color, String translationKey, double gravity, double velocity){
        return new StickData(material, color, translationKey, 1, gravity, velocity, true);
    }

    public Ingredient ingredient(){
        return Ingredient.of(material);
    }
}
