package com.coolerpromc.arrowplus.client.item;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record StickTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<StickTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("default").forGetter(StickTintSource::defaultColor)
    ).apply(instance, StickTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.has(ModDataComponents.ARROW_DATA.get())){
            Holder<ArrowData> holder = itemStack.get(ModDataComponents.ARROW_DATA.get());
            ArrowData data = holder.value();
            if (data.stick().value() instanceof ModStickItem && data.stickData().isPresent()){
                Holder<StickData> stickData = data.stickData().get();
                return stickData.value().color();
            }
            return 0xFF886627;
        }
        if (itemStack.getItem() instanceof ModStickItem item){
            Holder<StickData> stickData = itemStack.get(ModDataComponents.STICK_DATA.get());
            if (stickData != null){
                return stickData.value().color();
            }
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
