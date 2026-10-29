package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.datapack.ArrowMigrationCodec;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/MapCodec;recursive(Ljava/lang/String;Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"))
    private static MapCodec<ItemStack> arrowplus$migrateArrows(MapCodec<ItemStack> original) {
        return ArrowMigrationCodec.wrapStack(original);
    }
}
