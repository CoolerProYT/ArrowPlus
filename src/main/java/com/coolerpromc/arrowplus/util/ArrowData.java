package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects)
    ).apply(instance, ArrowData::new));

    public NbtCompound save(NbtCompound existingTag) {
        existingTag.put("arrow_data", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, System.err::println));
        return existingTag;
    }

    public static ArrowData load(NbtCompound tag) {
        if (!tag.contains("arrow_data", NbtElement.COMPOUND_TYPE)) {
            return ArrowData.EMPTY;
        }

        return CODEC.parse(NbtOps.INSTANCE, tag.get("arrow_data")).getOrThrow(false, System.err::println);
    }

    public static void encode(PacketByteBuf buf, ArrowData data){
        writeMaterial(buf, data.material);
        buf.writeDouble(data.baseDamage);
        buf.writeInt(data.color);
        buf.writeString(data.translationKey);
        buf.writeBoolean(data.flame);
        buf.writeDouble(data.gravity);
        buf.writeMap(data.effects, PacketByteBuf::writeIdentifier, PacketByteBuf::writeInt);
    }

    public static ArrowData decode(PacketByteBuf buf){
        return new ArrowData(readMaterial(buf), buf.readDouble(), buf.readInt(), buf.readString(), buf.readBoolean(), buf.readDouble(), buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readInt));
    }

    public static void writeMaterial(PacketByteBuf buf, Either<Identifier, TagKey<Item>> material) {
        buf.writeBoolean(material.left().isPresent());
        material.ifLeft(buf::writeIdentifier);
        material.ifRight(tag -> buf.writeIdentifier(tag.id()));
    }

    public static Either<Identifier, TagKey<Item>> readMaterial(PacketByteBuf buf) {
        boolean isLeft = buf.readBoolean();
        if (isLeft) {
            return Either.left(buf.readIdentifier());
        } else {
            return Either.right(TagKey.of(RegistryKeys.ITEM, buf.readIdentifier()));
        }
    }

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
