package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ArrowData(Either<Identifier, TagKey<Item>> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects) {
        this(Either.left(Registries.ITEM.getId(material)), baseDamage, color, translationKey, flame, gravity, effects);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects);
    }

    public static final Codec<Either<Identifier, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Identifier.CODEC,
            TagKey.codec(RegistryKeys.ITEM)
    );

    public static final PacketCodec<ByteBuf, Either<Identifier, TagKey<Item>>> MATERIAL_STREAM_CODEC = PacketCodecs.either(
            Identifier.PACKET_CODEC,
            Identifier.PACKET_CODEC.xmap(identifier -> TagKey.of(RegistryKeys.ITEM, identifier), TagKey::id)
    );

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects)
    ).apply(instance, ArrowData::new));

    public static final PacketCodec<PacketByteBuf, ArrowData> STREAM_CODEC = ExtraStreamCodecs.tuple(
            MATERIAL_STREAM_CODEC,
            ArrowData::material,
            PacketCodecs.DOUBLE,
            ArrowData::baseDamage,
            PacketCodecs.INTEGER,
            ArrowData::color,
            PacketCodecs.STRING,
            ArrowData::translationKey,
            PacketCodecs.BOOL,
            ArrowData::flame,
            PacketCodecs.DOUBLE,
            ArrowData::gravity,
            PacketCodecs.map(HashMap::new, Identifier.PACKET_CODEC, PacketCodecs.INTEGER),
            ArrowData::effects,
            ArrowData::new
    );

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
