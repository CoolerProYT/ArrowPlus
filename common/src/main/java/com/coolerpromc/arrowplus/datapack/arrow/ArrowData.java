package com.coolerpromc.arrowplus.datapack.arrow;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record ArrowData(Ingredient ingredient, double baseDamage, int color, String translationKey, boolean flame, double gravity, Map<Identifier, Integer> effects, Holder<Item> feather, Optional<Holder<FeatherData>> featherData, Holder<Item> stick, Optional<Holder<StickData>> stickData, int outputAmount) {
    public static final Codec<Holder<Item>> ITEM_HOLDER_CODEC = BuiltInRegistries.ITEM.holderByNameCodec().validate((entry) -> entry.is(Items.AIR.builtInRegistryHolder()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(entry));
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> ITEM_HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);

    public static final Codec<ArrowData> CODEC = RecordCodecBuilder.<ArrowData>create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("material").forGetter(ArrowData::ingredient),
            Codec.DOUBLE.fieldOf("baseDamage").forGetter(ArrowData::baseDamage),
            Codec.INT.fieldOf("color").forGetter(ArrowData::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ArrowData::translationKey),
            Codec.BOOL.fieldOf("flame").forGetter(ArrowData::flame),
            Codec.DOUBLE.fieldOf("gravity").forGetter(ArrowData::gravity),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("effects").forGetter(ArrowData::effects),
            ITEM_HOLDER_CODEC.fieldOf("feather").forGetter(ArrowData::feather),
            RegistryFixedCodec.create(ModRegistries.FEATHER_DATA_KEY).optionalFieldOf("featherData").forGetter(ArrowData::featherData),
            ITEM_HOLDER_CODEC.fieldOf("stick").forGetter(ArrowData::stick),
            RegistryFixedCodec.create(ModRegistries.STICK_DATA_KEY).optionalFieldOf("stickData").forGetter(ArrowData::stickData),
            Codec.INT.fieldOf("outputAmount").forGetter(ArrowData::outputAmount)
    ).apply(instance, ArrowData::new)).flatXmap(data -> {
        if (data.feather().value() instanceof ModFeatherItem && data.featherData().isEmpty()) {
            return DataResult.error(() -> "featherData is required when feather is a arrowplus:custom_feather");
        }
        if (data.stick().value() instanceof ModStickItem && data.stickData().isEmpty()) {
            return DataResult.error(() -> "stickData is required when stick is a arrowplus:custom_stick");
        }
        return DataResult.success(data);
    }, DataResult::success);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArrowData> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ArrowData::ingredient,
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
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.INT),
            ArrowData::effects,
            ITEM_HOLDER_STREAM_CODEC,
            ArrowData::feather,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(ModRegistries.FEATHER_DATA_KEY)),
            ArrowData::featherData,
            ITEM_HOLDER_STREAM_CODEC,
            ArrowData::stick,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(ModRegistries.STICK_DATA_KEY)),
            ArrowData::stickData,
            ByteBufCodecs.INT,
            ArrowData::outputAmount,
            ArrowData::new
    );

    public static final ArrowData EMPTY = ArrowData.builder(Items.BEDROCK).translationKey("item.arrowplus.cheated_item").build();

    public static Builder builder(Item material) {
        return new Builder(Ingredient.of(material));
    }

    public static Builder builder(TagKey<Item> material) {
        return new Builder(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(material)));
    }

    public static Builder builder(Ingredient material) {
        return new Builder(material);
    }

    public static class Builder {
        private final Ingredient material;
        private double baseDamage = 2.0;
        private int color = 0xFFFFFFFF;
        private String translationKey = "";
        private boolean flame = false;
        private double gravity = 0.05;
        private Map<Identifier, Integer> effects = Map.of();
        private Holder<Item> feather = Items.FEATHER.builtInRegistryHolder();
        private Holder<Item> stick = Items.STICK.builtInRegistryHolder();
        private int outputAmount = 4;
        private Optional<Holder<FeatherData>> featherData = Optional.empty();
        private Optional<Holder<StickData>> stickData = Optional.empty();

        private Builder(Ingredient material) {
            this.material = material;
        }

        public Builder baseDamage(double baseDamage) {
            this.baseDamage = baseDamage;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Builder flame(boolean flame) {
            this.flame = flame;
            return this;
        }

        public Builder flame() {
            this.flame = true;
            return this;
        }

        public Builder gravity(double gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder effects(Map<Identifier, Integer> effects) {
            this.effects = effects;
            return this;
        }

        public Builder feather(Item feather) {
            this.feather = feather.builtInRegistryHolder();
            return this;
        }

        public Builder stick(Item stick) {
            this.stick = stick.builtInRegistryHolder();
            return this;
        }

        public Builder outputAmount(int outputAmount) {
            this.outputAmount = outputAmount;
            return this;
        }

        public Builder featherData(Holder<FeatherData> location){
            this.featherData = Optional.of(location);
            return this;
        }

        public Builder stickData(Holder<StickData> location){
            this.stickData = Optional.of(location);
            return this;
        }

        public ArrowData build() {
            return new ArrowData(material, baseDamage, color, translationKey, flame, gravity, effects, feather, featherData, stick, stickData, outputAmount);
        }
    }

    public boolean isValidMaterial(ItemStack materialStack, ItemStack stickStack, ItemStack featherStack){
        return ingredient().test(materialStack) && stickStack.is(stick) && isValidFeather(featherStack);
    }

    public boolean isValidFeather(ItemStack featherStack){
        if (feather.value() instanceof ModFeatherItem){
            if (featherData.isPresent()){
                Holder<FeatherData> data = featherStack.getOrDefault(ModDataComponents.FEATHER_DATA.get(), Holder.direct(FeatherData.EMPTY));
                return featherStack.is(feather) && featherData.get().is(data);
            }
        }
        return featherStack.is(feather);
    }
}