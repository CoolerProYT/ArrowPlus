package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModArrowRenderer extends ArrowRenderer<ModArrowEntity> {
    public final ResourceLocation arrowTexture;
    public static final ResourceLocation BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final ResourceLocation HEAD_TEXTURE = getTextureLocation("arrow_plus_head");

    public ModArrowRenderer(EntityRendererProvider.Context context, ResourceLocation arrowTexture) {
        super(context);
        this.arrowTexture = arrowTexture;
    }

    public static ResourceLocation getTextureLocation(String textureName) {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void render(ModArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        float f9 = (float)entity.shakeTime - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutout(HEAD_TEXTURE));
        PoseStack.Pose posestack$pose = poseStack.last();
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight, entity.getColor());
        this.setVertex(posestack$pose, vertexconsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight, entity.getColor());

        for(int j = 0; j < 4; ++j) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.setVertex(posestack$pose, vertexconsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight, entity.getColor());
            this.setVertex(posestack$pose, vertexconsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight, entity.getColor());
            this.setVertex(posestack$pose, vertexconsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight, entity.getColor());
            this.setVertex(posestack$pose, vertexconsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight, entity.getColor());
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void setVertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, int color) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(ModArrowEntity arrowEntity) {
        return BODY_TEXTURE;
    }
}
