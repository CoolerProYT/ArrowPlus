package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
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

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects)
    ).apply(instance, ArrowData::new));

    public CompoundTag save(CompoundTag existingTag) {
        existingTag.put("arrow_data", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, System.err::println));
        return existingTag;
    }

    public static ArrowData load(CompoundTag tag) {
        if (!tag.contains("arrow_data", CompoundTag.TAG_COMPOUND)) {
            return ArrowData.EMPTY;
        }

        return CODEC.parse(NbtOps.INSTANCE, tag.get("arrow_data")).getOrThrow(false, System.err::println);
    }

    public static void encode(FriendlyByteBuf buf, ArrowData data){
        writeMaterial(buf, data.material);
        buf.writeDouble(data.baseDamage);
        buf.writeInt(data.color);
        buf.writeUtf(data.translationKey);
        buf.writeBoolean(data.flame);
        buf.writeDouble(data.gravity);
        buf.writeMap(data.effects, FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::writeInt);
    }

    public static ArrowData decode(FriendlyByteBuf buf){
        return new ArrowData(readMaterial(buf), buf.readDouble(), buf.readInt(), buf.readUtf(), buf.readBoolean(), buf.readDouble(), buf.readMap(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::readInt));
    }

    public static void writeMaterial(FriendlyByteBuf buf, Either<ResourceLocation, TagKey<Item>> material) {
        buf.writeBoolean(material.left().isPresent());
        material.ifLeft(buf::writeResourceLocation);
        material.ifRight(tag -> buf.writeResourceLocation(tag.location()));
    }

    public static Either<ResourceLocation, TagKey<Item>> readMaterial(FriendlyByteBuf buf) {
        boolean isLeft = buf.readBoolean();
        if (isLeft) {
            return Either.left(buf.readResourceLocation());
        } else {
            return Either.right(TagKey.create(Registries.ITEM, buf.readResourceLocation()));
        }
    }

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
