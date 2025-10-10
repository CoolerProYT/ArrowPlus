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

import java.util.HashMap;
import java.util.Map;

public record ArrowData(Either<ResourceLocation, TagKey<Item>> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects) {
        this(Either.left(BuiltInRegistries.ITEM.getKey(material)), baseDamage, color, translationKey, flame, gravity, effects);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects);
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
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects)
    ).apply(instance, ArrowData::new));

    public static final StreamCodec<FriendlyByteBuf, ArrowData> STREAM_CODEC = ExtraStreamCodecs.composite(
            MATERIAL_STREAM_CODEC,
            ArrowData::material,
            ByteBufCodecs.DOUBLE,
            ArrowData::baseDamage,
            ByteBufCodecs.INT,
            ArrowData::color,
            ByteBufCodecs.STRING_UTF8,
            ArrowData::translationKey,
            ByteBufCodecs.BOOL,
            ArrowData::flame,
            ByteBufCodecs.DOUBLE,
            ArrowData::gravity,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT),
            ArrowData::effects,
            ArrowData::new
    );

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
