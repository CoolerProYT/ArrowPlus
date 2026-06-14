package com.coolerpromc.arrowplus.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ProjectileWeaponItemHelper {
    public static boolean useAmmo(boolean original, ItemStack weapon, ItemStack projectile, LivingEntity holder){
        return original || projectile.getItem() instanceof InfiniteArrow ai && ai.isInfinite(projectile, weapon, holder);
    }
}
