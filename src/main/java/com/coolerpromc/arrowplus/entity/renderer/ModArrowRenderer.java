package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ModArrowRenderer extends EntityRenderer<ModArrowEntity> {
    public static final Identifier BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final Identifier HEAD_TEXTURE = getTextureLocation("arrow_plus_head");
    public static final Identifier FEATHER_TEXTURE = getTextureLocation("arrow_plus_feather");

    public ModArrowRenderer(EntityRendererFactory.Context p_174399_) {
        super(p_174399_);
    }

    public static Identifier getTextureLocation(String textureName) {
        return Identifier.of(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void render(ModArrowEntity entity, float entityYaw, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight) {
        int bodyColor = 0xFF886627;
        int featherColor = -1;

        if (entity.getArrowData().stick().value() instanceof ModStickItem item){
            bodyColor = item.getColor();
        }
        if (entity.getArrowData().feather().value() instanceof ModFeatherItem item){
            featherColor = item.getColor();
        }

        render(entity, partialTicks, poseStack, buffer, packedLight, BODY_TEXTURE, bodyColor);
        render(entity, partialTicks, poseStack, buffer, packedLight, HEAD_TEXTURE, entity.getArrowData().color());
        render(entity, partialTicks, poseStack, buffer, packedLight, FEATHER_TEXTURE, featherColor);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void render(ModArrowEntity entity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight, Identifier texture, int color){
        poseStack.push();
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevYaw, entity.getYaw()) - 90.0F));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevPitch, entity.getPitch())));
        float f9 = (float)entity.shake - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f10));
        }

        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderLayer.getEntityCutout(texture));
        MatrixStack.Entry posestack$pose = poseStack.peek();
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight, color);
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight, color);

        for(int j = 0; j < 4; ++j) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            this.setVertex(posestack$pose, vertexconsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight, color);
            this.setVertex(posestack$pose, vertexconsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight, color);
            this.setVertex(posestack$pose, vertexconsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight, color);
            this.setVertex(posestack$pose, vertexconsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight, color);
        }

        poseStack.pop();
    }


    public void setVertex(MatrixStack.Entry pose, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, int color) {
        consumer.vertex(pose, (float)x, (float)y, (float)z).color(color).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

    @Override
    public Identifier getTexture(ModArrowEntity entity) {
        return BODY_TEXTURE;
    }
}
