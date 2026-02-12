package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ModArrowRenderer extends EntityRenderer<ModArrowEntity, ModArrowRenderState> {
    private final ArrowModel model;
    public static final Identifier BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final Identifier HEAD_TEXTURE = getTextureLocation("arrow_plus_head");
    public static final Identifier FEATHER_TEXTURE = getTextureLocation("arrow_plus_feather");

    public ModArrowRenderer(EntityRendererProvider.Context p_174399_) {
        super(p_174399_);
        this.model = new ArrowModel(p_174399_.bakeLayer(ModelLayers.ARROW));
    }

    public ModArrowRenderState createRenderState() {
        return new ModArrowRenderState();
    }

    public static Identifier getTextureLocation(String textureName) {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void submit(ModArrowRenderState renderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderTypes.entityCutout(BODY_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.bodyColor, null, renderState.outlineColor, null);
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderTypes.entityCutout(HEAD_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.headColor, null, renderState.outlineColor, null);
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderTypes.entityCutout(FEATHER_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.featherColor, null, renderState.outlineColor, null);
        matrixStack.popPose();
        super.submit(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    @Override
    public void extractRenderState(ModArrowEntity arrowEntity, ModArrowRenderState renderState, float p_360538_) {
        super.extractRenderState(arrowEntity, renderState, p_360538_);
        renderState.headColor  = arrowEntity.getArrowData().color();

        if (arrowEntity.getArrowData().stick().value() instanceof ModStickItem item){
            renderState.bodyColor = item.getColor();
        }
        else{
            renderState.bodyColor = 0xFF886627;
        }
        if (arrowEntity.getArrowData().feather().value() instanceof ModFeatherItem item){
            renderState.featherColor = item.getColor();
        }
        else{
            renderState.featherColor = -1;
        }
        renderState.xRot = arrowEntity.getViewXRot(p_360538_);
        renderState.yRot = arrowEntity.getViewYRot(p_360538_);
        renderState.shake = (float)arrowEntity.shakeTime - p_360538_;
    }
}
