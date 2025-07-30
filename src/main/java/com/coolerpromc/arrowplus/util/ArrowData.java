package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
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

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey)
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

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item");
}
