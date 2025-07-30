package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedWeaponItem.class)
public abstract class InfiniteBowMixin {
    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private static void getProjectiles(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cir) {
        if (multishot || shooter instanceof PlayerEntity player && player.isCreative()) {
            return;
        }

        if (projectileStack.getItem() instanceof InfiniteArrow arrowItem && arrowItem.isInfinite(projectileStack, stack, shooter)) {
            ItemStack intangible = projectileStack.copyWithCount(1);
            intangible.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            cir.setReturnValue(intangible);
        }
    }
}