package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

public record BowTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<BowTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("default").forGetter(BowTintSource::defaultColor)
    ).apply(instance, BowTintSource::new));

    @Override
    public int getTint(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity) {
        if (livingEntity != null){
            ItemStack arrowStack = livingEntity.getProjectileType(itemStack);
            if (arrowStack.getItem() == Items.ARROW){
                return 0xFF141414;
            }
            ArrowData data = arrowStack.get(ModDataComponents.ARROW_DATA);
            if (data != null) {
                return data.color();
            }
        }
        return -1;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}
