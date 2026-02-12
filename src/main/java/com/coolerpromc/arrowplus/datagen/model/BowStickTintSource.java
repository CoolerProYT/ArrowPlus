package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BowStickTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<BowStickTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("default").forGetter(BowStickTintSource::defaultColor)
    ).apply(instance, BowStickTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (livingEntity != null){
            ItemStack arrowStack = livingEntity.getProjectile(itemStack);
            Holder<ArrowData> data = arrowStack.get(ModDataComponents.ARROW_DATA);
            if (data != null && data.value().stick().value() instanceof ModStickItem item){
                return item.getColor();
            }
            return 0xFF886627;
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}