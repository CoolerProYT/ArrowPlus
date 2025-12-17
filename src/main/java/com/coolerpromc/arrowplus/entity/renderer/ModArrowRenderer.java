package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ModArrowRenderer extends ArrowRenderer<ModArrowEntity, ModArrowRenderState> {
    private final ArrowModel model;
    public final Identifier arrowTexture;
    public static final Identifier BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final Identifier HEAD_TEXTURE = getTextureLocation("arrow_plus_head");

    public ModArrowRenderer(EntityRendererProvider.Context p_174399_, Identifier arrowTexture) {
        super(p_174399_);
        this.arrowTexture = arrowTexture;
        this.model = new ArrowModel(p_174399_.bakeLayer(ModelLayers.ARROW));
    }

    protected Identifier getTextureLocation(ModArrowRenderState p_364566_) {
        return BODY_TEXTURE;
    }

    public ModArrowRenderState createRenderState() {
        return new ModArrowRenderState();
    }

    public static Identifier getTextureLocation(String textureName) {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void submit(ModArrowRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        nodeCollector.submitModel(this.model, renderState, poseStack, RenderTypes.entityCutout(this.getTextureLocation(renderState)), renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
        nodeCollector.submitModel(this.model, renderState, poseStack, RenderTypes.entityCutout(HEAD_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.color, null, renderState.outlineColor, null);
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    @Override
    public void extractRenderState(ModArrowEntity arrowEntity, ModArrowRenderState renderState, float p_360538_) {
        super.extractRenderState(arrowEntity, renderState, p_360538_);
        renderState.color = arrowEntity.getArrowData().color();
    }
}