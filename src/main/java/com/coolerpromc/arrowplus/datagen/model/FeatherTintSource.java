package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

public record FeatherTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<FeatherTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.ARGB.fieldOf("default").forGetter(FeatherTintSource::defaultColor)
    ).apply(instance, FeatherTintSource::new));

    @Override
    public int getTint(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.contains(ModDataComponents.ARROW_DATA)){
            RegistryEntry<ArrowData> holder = itemStack.get(ModDataComponents.ARROW_DATA);
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
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}