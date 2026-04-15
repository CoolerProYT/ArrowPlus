package com.coolerpromc.arrowplus.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface InfiniteArrow {
    boolean isInfinite(ItemStack arrowStack, ItemStack bowStack, LivingEntity shooter);
}