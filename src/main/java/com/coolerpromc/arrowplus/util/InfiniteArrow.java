package com.coolerpromc.arrowplus.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface InfiniteArrow {
    boolean isInfinite(ItemStack arrowStack, ItemStack bowStack, LivingEntity shooter);
}