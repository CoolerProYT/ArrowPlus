package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ModArrowRenderer extends EntityRenderer<ModArrowEntity, ModArrowRenderState> {
    private final ArrowEntityModel model;
    public static final Identifier BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final Identifier HEAD_TEXTURE = getTextureLocation("arrow_plus_head");
    public static final Identifier FEATHER_TEXTURE = getTextureLocation("arrow_plus_feather");

    public ModArrowRenderer(EntityRendererFactory.Context p_174399_) {
        super(p_174399_);
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
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderLayers.entityCutout(BODY_TEXTURE), renderState.light, OverlayTexture.DEFAULT_UV, renderState.bodyColor, null, renderState.outlineColor, null);
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderLayers.entityCutout(HEAD_TEXTURE), renderState.light, OverlayTexture.DEFAULT_UV, renderState.headColor, null, renderState.outlineColor, null);
        orderedRenderCommandQueue.submitModel(this.model, renderState, matrixStack, RenderLayers.entityCutout(FEATHER_TEXTURE), renderState.light, OverlayTexture.DEFAULT_UV, renderState.featherColor, null, renderState.outlineColor, null);
        matrixStack.pop();
        super.render(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    @Override
    public void updateRenderState(ModArrowEntity arrowEntity, ModArrowRenderState renderState, float p_360538_) {
        super.updateRenderState(arrowEntity, renderState, p_360538_);
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
        renderState.pitch = arrowEntity.getPitch(p_360538_);
        renderState.yaw = arrowEntity.getYaw(p_360538_);
        renderState.shake = (float)arrowEntity.shake - p_360538_;
    }
}
