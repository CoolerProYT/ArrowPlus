package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ArrowTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<ArrowTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("default").forGetter(ArrowTintSource::defaultColor)
    ).apply(instance, ArrowTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        Holder<ArrowData> arrowData = itemStack.get(ModDataComponents.ARROW_DATA.get());
        if (arrowData != null){
            return ARGB.opaque(arrowData.value().color());
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
