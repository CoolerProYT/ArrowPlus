package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record ArrowData(Either<ResourceLocation, TagKey<Item>> material, double baseDamage, int color, String translationKey) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey) {
        this(Either.left(BuiltInRegistries.ITEM.getKey(material)), baseDamage, color, translationKey);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey) {
        this(Either.right(material), baseDamage, color, translationKey);
    }

    public static final Codec<Either<ResourceLocation, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            ResourceLocation.CODEC,
            TagKey.hashedCodec(Registries.ITEM)
    );

    public static final StreamCodec<ByteBuf, Either<ResourceLocation, TagKey<Item>>> MATERIAL_STREAM_CODEC = ByteBufCodecs.either(
            ResourceLocation.STREAM_CODEC,
            ResourceLocation.STREAM_CODEC.map(p_372701_ -> TagKey.create(Registries.ITEM, p_372701_), TagKey::location)
    );

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey)
    ).apply(instance, ArrowData::new));

    public static final StreamCodec<FriendlyByteBuf, ArrowData> STREAM_CODEC = StreamCodec.composite(
            MATERIAL_STREAM_CODEC,
            ArrowData::material,
            ByteBufCodecs.DOUBLE,
            ArrowData::baseDamage,
            ByteBufCodecs.INT,
            ArrowData::color,
            ByteBufCodecs.STRING_UTF8,
            ArrowData::translationKey,
            ArrowData::new
    );

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item");
}
