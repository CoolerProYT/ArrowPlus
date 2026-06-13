package com.coolerpromc.arrowplus.datapack.feather;

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

public record FeatherData(HolderSet<Item> material, int color, String translationKey, int outputAmount) {
    public static final Codec<FeatherData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.ITEM_HOLDER_SET_CODEC.fieldOf("material").forGetter(FeatherData::material),
            Codec.INT.fieldOf("color").forGetter(FeatherData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(FeatherData::translationKey),
            Codec.INT.optionalFieldOf("outputAmount", 4).forGetter(FeatherData::outputAmount)
    ).apply(i, FeatherData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FeatherData> STREAM_CODEC = StreamCodec.composite(
            Codecs.ITEM_HOLDER_SET_STREAM_CODEC,
            FeatherData::material,
            ByteBufCodecs.INT,
            FeatherData::color,
            ByteBufCodecs.STRING_UTF8,
            FeatherData::translationKey,
            ByteBufCodecs.INT,
            FeatherData::outputAmount,
            FeatherData::new
    );
    public static final FeatherData EMPTY = new FeatherData(Items.AIR, -1, "item.arrowplus.cheated_item");

    public FeatherData(ItemLike material, int color, String translationKey){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, 4);
    }

    public FeatherData(TagKey<Item> material, int color, String translationKey){
        this(BuiltInRegistries.ITEM.getOrCreateTag(material), color, translationKey, 4);
    }

    public FeatherData(ItemLike material, int color, String translationKey, int outputAmount){
        this(HolderSet.direct(material.asItem().builtInRegistryHolder()), color, translationKey, outputAmount);
    }

    public FeatherData(TagKey<Item> material, int color, String translationKey, int outputAmount){
        this(BuiltInRegistries.ITEM.getOrCreateTag(material), color, translationKey, outputAmount);
    }
}
