package com.coolerpromc.arrowplus.datapack.stick;

import com.coolerpromc.arrowplus.util.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public record StickData(HolderSet<Item> material, int color, String translationKey, int outputAmount) {
    public static final Codec<StickData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.ITEM_HOLDER_SET_CODEC.fieldOf("material").forGetter(StickData::material),
            Codec.INT.fieldOf("color").forGetter(StickData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(StickData::translationKey),
            Codec.INT.fieldOf("outputAmount").forGetter(StickData::outputAmount)
    ).apply(i, StickData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StickData> STREAM_CODEC = StreamCodec.composite(
            Codecs.ITEM_HOLDER_SET_STREAM_CODEC,
            StickData::material,
            ByteBufCodecs.INT,
            StickData::color,
            ByteBufCodecs.STRING_UTF8,
            StickData::translationKey,
            ByteBufCodecs.INT,
            StickData::outputAmount,
            StickData::new
    );

    public static final StickData EMPTY = new StickData(Items.AIR, -1, "item.arrowplus.cheated_item");

    public StickData(ItemLike material, int color, String translationKey){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, 4);
    }

    public StickData(TagKey<Item> material, int color, String translationKey){
        this(BuiltInRegistries.ITEM.getOrCreateTag(material), color, translationKey, 4);
    }

    public StickData(ItemLike material, int color, String translationKey, int outputAmount){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, outputAmount);
    }

    public StickData(TagKey<Item> material, int color, String translationKey, int outputAmount){
        this(BuiltInRegistries.ITEM.getOrCreateTag(material), color, translationKey, outputAmount);
    }
}
