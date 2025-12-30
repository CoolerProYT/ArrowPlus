package com.coolerpromc.arrowplus.datagen.model;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record TippedCondition() implements BooleanProperty {
    public static final MapCodec<TippedCondition> MAP_CODEC = MapCodec.unit(new TippedCondition());

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return MAP_CODEC;
    }

    @Override
    public boolean test(ItemStack itemStack, @Nullable ClientWorld clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return itemStack.contains(DataComponentTypes.POTION_CONTENTS);
    }
}