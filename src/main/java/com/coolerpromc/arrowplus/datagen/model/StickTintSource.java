package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;
import org.jspecify.annotations.Nullable;

public record StickTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<StickTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.ARGB.fieldOf("default").forGetter(StickTintSource::defaultColor)
    ).apply(instance, StickTintSource::new));

    @Override
    public int getTint(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.contains(ModDataComponents.ARROW_DATA)){
            RegistryEntry<ArrowData> holder = itemStack.get(ModDataComponents.ARROW_DATA);
            ArrowData data = holder.value();
            if (data.stick().value() instanceof ModStickItem item){
                return item.getColor();
            }
            return 0xFF886627;
        }
        if (itemStack.getItem() instanceof ModStickItem item){
            return item.getColor();
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}