package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.99F))
    private float arrowplus$airDrag(float drag) {
        if ((Object) this instanceof ModArrowEntity arrow){
            return (float) arrow.getFeatherData().airDrag();
        }
        return drag;
    }
}
