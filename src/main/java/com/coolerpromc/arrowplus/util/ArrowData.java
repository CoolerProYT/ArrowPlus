package com.coolerpromc.arrowplus.util;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public record ArrowData(ResourceLocation material, double baseDamage, int color, String translationKey) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey) {
        this(BuiltInRegistries.ITEM.getKey(material), baseDamage, color, translationKey);
    }

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey)
    ).apply(instance, ArrowData::new));

    public static final StreamCodec<FriendlyByteBuf, ArrowData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ArrowData::material,
            ByteBufCodecs.DOUBLE,
            ArrowData::baseDamage,
            ByteBufCodecs.INT,
            ArrowData::color,
            ByteBufCodecs.STRING_UTF8,
            ArrowData::translationKey,
            ArrowData::new
    );

    public static final List<ItemStack> VARIANT_STACKS = new ArrayList<>();
    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item");
}
