package com.coolerpromc.arrowplus.arrow;

import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.util.StreamCodecs;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ArrowData(Either<RegistryEntry<Item>, TagKey<Item>> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects, RegistryEntry<Item> feather, RegistryEntry<Item> stick, int outputAmount) {
    public static final Codec<RegistryEntry<Item>> ITEM_HOLDER_CODEC = Registries.ITEM.getEntryCodec().validate((entry) -> entry.matches(Items.AIR.getRegistryEntry()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(entry));
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Item>> ITEM_HOLDER_STREAM_CODEC = PacketCodecs.registryEntry(RegistryKeys.ITEM);

    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects) {
        this(Either.left(material.getRegistryEntry()), baseDamage, color, translationKey, flame, gravity, effects, Items.FEATHER.getRegistryEntry(), Items.STICK.getRegistryEntry(), 4);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects, Items.FEATHER.getRegistryEntry(), Items.STICK.getRegistryEntry(), 4);
    }

    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects, Item feather, Item stick, int outputAmount) {
        this(Either.left(material.getRegistryEntry()), baseDamage, color, translationKey, flame, gravity, effects, feather.getRegistryEntry(), stick.getRegistryEntry(), outputAmount);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects, Item feather, Item stick, int outputAmount) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects, feather.getRegistryEntry(), stick.getRegistryEntry(), outputAmount);
    }

    public boolean isValidMaterial(ItemStack materialStack, ItemStack stickStack, ItemStack featherStack){
        if (material.left().isPresent() && materialStack.itemMatches(material.left().get())){
            return stickStack.itemMatches(stick) && featherStack.itemMatches(feather);
        }
        else if (material.right().isPresent() && materialStack.isIn(material.right().get())){
            return stickStack.itemMatches(stick) && featherStack.itemMatches(feather);
        }
        else {
            return false;
        }
    }

    public static final Codec<Either<RegistryEntry<Item>, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            ITEM_HOLDER_CODEC,
            TagKey.codec(RegistryKeys.ITEM)
    );

    public static final PacketCodec<RegistryByteBuf, Either<RegistryEntry<Item>, TagKey<Item>>> MATERIAL_STREAM_CODEC = PacketCodecs.either(
            ITEM_HOLDER_STREAM_CODEC,
            Identifier.PACKET_CODEC.xmap(p_372701_ -> TagKey.of(RegistryKeys.ITEM, p_372701_), TagKey::id)
    );

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects),
            ITEM_HOLDER_CODEC.fieldOf("feather").validate(itemHolder -> itemHolder.matches(Items.FEATHER.getRegistryEntry()) || itemHolder.value() instanceof ModFeatherItem ? DataResult.success(itemHolder) : DataResult.error(() -> "Item must be vanilla feather or feather from Arrow+ mod.")).forGetter(ArrowData::feather),
            ITEM_HOLDER_CODEC.fieldOf("stick").validate(itemHolder -> itemHolder.matches(Items.STICK.getRegistryEntry()) || itemHolder.value() instanceof ModStickItem ? DataResult.success(itemHolder) : DataResult.error(() -> "Item must be vanilla stick or stick from Arrow+ mod.")).forGetter(ArrowData::stick),
            Codec.INT.fieldOf("outputAmount").forGetter(ArrowData::outputAmount)
    ).apply(instance, ArrowData::new));

    public static final PacketCodec<RegistryByteBuf, ArrowData> STREAM_CODEC = StreamCodecs.tuple(
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
            ITEM_HOLDER_STREAM_CODEC,
            ArrowData::feather,
            ITEM_HOLDER_STREAM_CODEC,
            ArrowData::stick,
            PacketCodecs.INTEGER,
            ArrowData::outputAmount,
            ArrowData::new
    );

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
