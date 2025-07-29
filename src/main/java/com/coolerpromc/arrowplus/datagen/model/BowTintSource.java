package com.coolerpromc.arrowplus.datagen.model;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public record BowTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<BowTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("default").forGetter(BowTintSource::defaultColor)
    ).apply(instance, BowTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (livingEntity != null){
            ItemStack arrowStack = livingEntity.getProjectile(itemStack);
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
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
