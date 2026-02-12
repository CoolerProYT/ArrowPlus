package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public abstract class InfiniteBowMixin {
    @Inject(method = "useAmmo", at = @At("HEAD"), cancellable = true)
    private static void getProjectiles(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cir) {
        if (multishot || shooter instanceof Player player && player.isCreative()) {
            return;
        }

        if (projectileStack.getItem() instanceof InfiniteArrow arrowItem && arrowItem.isInfinite(projectileStack, stack, shooter)) {
            ItemStack intangible = projectileStack.copyWithCount(1);
            intangible.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            cir.setReturnValue(intangible);
        }
    }
}