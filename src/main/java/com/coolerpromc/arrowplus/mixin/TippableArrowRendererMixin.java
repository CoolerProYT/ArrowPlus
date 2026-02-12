package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.ArrowPlus;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TippableArrowRenderer.class)
public class TippableArrowRendererMixin {
    @Redirect(method = "getTextureLocation",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/TippableArrowRenderer;NORMAL_ARROW_LOCATION:Lnet/minecraft/resources/Identifier;"
            )
    )
    private Identifier redirectArrowTexture() {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "textures/entity/projectiles/arrow.png");
    }
}
