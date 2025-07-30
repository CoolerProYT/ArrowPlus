package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArrowEntityRenderer.class)
public class TippableArrowRendererMixin {
    @Redirect(method = "getTexture*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/entity/ArrowEntityRenderer;TEXTURE:Lnet/minecraft/util/Identifier;"
            )
    )
    private Identifier redirectArrowTexture() {
        return Identifier.of(ArrowPlus.MODID, "textures/entity/projectiles/arrow.png");
    }
}
