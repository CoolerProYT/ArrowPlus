package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

public record ArrowTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<ArrowTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.ARGB.fieldOf("default").forGetter(ArrowTintSource::defaultColor)
    ).apply(instance, ArrowTintSource::new));

    @Override
    public int getTint(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity) {
        RegistryEntry<ArrowData> arrowData = itemStack.get(ModDataComponents.ARROW_DATA);
        if (arrowData != null){
            return ColorHelper.fullAlpha(arrowData.value().color());
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}
