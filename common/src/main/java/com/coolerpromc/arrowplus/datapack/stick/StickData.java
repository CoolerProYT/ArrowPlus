package com.coolerpromc.arrowplus.datapack.stick;

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

public record StickData(HolderSet<Item> material, int color, String translationKey, int outputAmount) {
    public static final Codec<StickData> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("material").forGetter(StickData::material),
            Codec.INT.fieldOf("color").forGetter(StickData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(StickData::translationKey),
            Codec.INT.fieldOf("outputAmount").forGetter(StickData::outputAmount)
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
            StickData::new
    );

    public static final StickData EMPTY = new StickData(Items.BEDROCK, -1, "item.arrowplus.cheated_item");

    public StickData(ItemLike material, int color, String translationKey){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, 4);
    }

    public StickData(TagKey<Item> material, int color, String translationKey){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, 4);
    }

    public StickData(ItemLike material, int color, String translationKey, int outputAmount){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, outputAmount);
    }

    public StickData(TagKey<Item> material, int color, String translationKey, int outputAmount){
        this(BuiltInRegistries.ITEM.getOrThrow(material), color, translationKey, outputAmount);
    }

    public Ingredient ingredient(){
        return Ingredient.of(material);
    }
}