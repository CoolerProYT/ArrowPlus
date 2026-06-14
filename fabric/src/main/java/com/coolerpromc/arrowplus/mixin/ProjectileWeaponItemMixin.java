package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.ProjectileWeaponItemHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
    @WrapOperation(method = "useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasInfiniteMaterials()Z"))
    private static boolean modifyInfiniteMaterials(LivingEntity entity, Operation<Boolean> original, ItemStack weapon, ItemStack projectile, LivingEntity holder, boolean forceInfinite) {
        return ProjectileWeaponItemHelper.useAmmo(original.call(entity), weapon, projectile, holder);
    }
}
