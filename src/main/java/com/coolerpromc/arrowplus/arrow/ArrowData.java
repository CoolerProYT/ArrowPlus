package com.coolerpromc.arrowplus.arrow;

import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.util.StreamCodecs;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"NullableProblems", "deprecation"})
public record ArrowData(Either<Holder<Item>, TagKey<Item>> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects, Holder<Item> feather, Holder<Item> stick, int outputAmount) {
    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects) {
        this(Either.left(material.builtInRegistryHolder()), baseDamage, color, translationKey, flame, gravity, effects, Items.FEATHER.builtInRegistryHolder(), Items.STICK.builtInRegistryHolder(), 4);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects, Items.FEATHER.builtInRegistryHolder(), Items.STICK.builtInRegistryHolder(), 4);
    }

    public ArrowData(Item material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects, Item feather, Item stick, int outputAmount) {
        this(Either.left(material.builtInRegistryHolder()), baseDamage, color, translationKey, flame, gravity, effects, feather.builtInRegistryHolder(), stick.builtInRegistryHolder(), outputAmount);
    }

    public ArrowData(TagKey<Item> material, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<ResourceLocation, Integer> effects, Item feather, Item stick, int outputAmount) {
        this(Either.right(material), baseDamage, color, translationKey, flame, gravity, effects, feather.builtInRegistryHolder(), stick.builtInRegistryHolder(), outputAmount);
    }

    public boolean isValidMaterial(ItemStack materialStack, ItemStack stickStack, ItemStack featherStack){
        if (material.left().isPresent() && materialStack.is(material.left().get())){
            return stickStack.is(stick) && featherStack.is(feather);
        }
        else if (material.right().isPresent() && materialStack.is(material.right().get())){
            return stickStack.is(stick) && featherStack.is(feather);
        }
        else {
            return false;
        }
    }

    public static final Codec<Either<Holder<Item>, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Item.CODEC,
            TagKey.hashedCodec(Registries.ITEM)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Either<Holder<Item>, TagKey<Item>>> MATERIAL_STREAM_CODEC = ByteBufCodecs.either(
            Item.STREAM_CODEC,
            TagKey.streamCodec(Registries.ITEM)
    );

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ArrowData::material),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects),
            Item.CODEC.fieldOf("feather").validate(itemHolder -> itemHolder.is(Items.FEATHER.builtInRegistryHolder()) || itemHolder.value() instanceof ModFeatherItem ? DataResult.success(itemHolder) : DataResult.error(() -> "Item must be vanilla feather or feather from Arrow+ mod.")).forGetter(ArrowData::feather),
            Item.CODEC.fieldOf("stick").validate(itemHolder -> itemHolder.is(Items.STICK.builtInRegistryHolder()) || itemHolder.value() instanceof ModStickItem ? DataResult.success(itemHolder) : DataResult.error(() -> "Item must be vanilla stick or stick from Arrow+ mod.")).forGetter(ArrowData::stick),
            Codec.INT.fieldOf("outputAmount").forGetter(ArrowData::outputAmount)
    ).apply(instance, ArrowData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArrowData> STREAM_CODEC = StreamCodecs.composite(
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
            Item.STREAM_CODEC,
            ArrowData::feather,
            Item.STREAM_CODEC,
            ArrowData::stick,
            ByteBufCodecs.INT,
            ArrowData::outputAmount,
            ArrowData::new
    );

    public static final ArrowData EMPTY = new ArrowData(Items.AIR, 0.0, 0xFF141414, "item.arrowplus.cheated_item", false, 0.05, Map.of());
}
