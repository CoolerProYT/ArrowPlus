package com.coolerpromc.arrowplus.client.item;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record BowStickTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<BowStickTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("default").forGetter(BowStickTintSource::defaultColor)
    ).apply(instance, BowStickTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (livingEntity != null){
            ItemStack arrowStack = livingEntity.getProjectile(itemStack);
            if (arrowStack.has(ModDataComponents.ARROW_DATA.get())){
                return ArrowParts.stick(arrowStack).color();
            }
            return StickData.VANILLA_COLOR;
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
