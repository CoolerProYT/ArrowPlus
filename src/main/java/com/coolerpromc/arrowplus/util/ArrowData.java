package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public record ArrowData(Either<Identifier, TagKey<Item>> material, double baseDamage, int color, String translationKey) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey) {
        this(Either.left(Registries.ITEM.getId(material)), baseDamage, color, translationKey);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey) {
        this(Either.right(material), baseDamage, color, translationKey);
    }

    public static final Codec<Either<Identifier, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Identifier.CODEC,
            TagKey.codec(RegistryKeys.ITEM)
    );

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey)
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

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item");
}
