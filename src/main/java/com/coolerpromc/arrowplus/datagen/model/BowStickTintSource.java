package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public record BowStickTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<BowStickTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("default").forGetter(BowStickTintSource::defaultColor)
    ).apply(instance, BowStickTintSource::new));

    @Override
    public int getTint(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity) {
        if (livingEntity != null){
            ItemStack arrowStack = livingEntity.getProjectileType(itemStack);
            RegistryEntry<ArrowData> data = arrowStack.get(ModDataComponents.ARROW_DATA);
            if (data != null && data.value().stick().value() instanceof ModStickItem item){
                return item.getColor();
            }
            return 0xFF886627;
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}