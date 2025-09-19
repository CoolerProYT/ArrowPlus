package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ModArrowRenderer extends ProjectileEntityRenderer<ModArrowEntity, ModArrowRenderState> {
    private final ArrowEntityModel model;
    public final Identifier arrowTexture;
    public static final Identifier BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final Identifier HEAD_TEXTURE = getTextureLocation("arrow_plus_head");

    public ModArrowRenderer(EntityRendererFactory.Context p_174399_, Identifier arrowTexture) {
        super(p_174399_);
        this.arrowTexture = arrowTexture;
        this.model = new ArrowEntityModel(p_174399_.getPart(EntityModelLayers.ARROW));
    }

    public ModArrowRenderState createRenderState() {
        return new ModArrowRenderState();
    }

    public static Identifier getTextureLocation(String textureName) {
        return Identifier.of(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void render(ModArrowRenderState renderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(renderState.yaw - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(renderState.pitch));
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderLayer.getEntityCutout(this.getTexture(renderState)), renderState.light, OverlayTexture.DEFAULT_UV, -1, null, renderState.outlineColor, null);
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderLayer.getEntityCutout(HEAD_TEXTURE), renderState.light, OverlayTexture.DEFAULT_UV, renderState.color, null, renderState.outlineColor, null);
        matrixStack.pop();
        super.render(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    @Override
    protected Identifier getTexture(ModArrowRenderState state) {
        return BODY_TEXTURE;
    }

    @Override
    public void updateRenderState(ModArrowEntity arrowEntity, ModArrowRenderState renderState, float p_360538_) {
        super.updateRenderState(arrowEntity, renderState, p_360538_);
        renderState.color = arrowEntity.getColor();
    }
}
