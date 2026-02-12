package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FeatherTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<FeatherTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("default").forGetter(FeatherTintSource::defaultColor)
    ).apply(instance, FeatherTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.has(ModDataComponents.ARROW_DATA)){
            Holder<ArrowData> holder = itemStack.get(ModDataComponents.ARROW_DATA);
            ArrowData data = holder.value();
            if (data.feather().value() instanceof ModFeatherItem item){
                return item.getColor();
            }
            return -1;
        }
        if (itemStack.getItem() instanceof ModFeatherItem item){
            return item.getColor();
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}