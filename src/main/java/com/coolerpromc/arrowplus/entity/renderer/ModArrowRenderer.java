package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ModArrowRenderer extends ArrowRenderer<ModArrowEntity, ModArrowRenderState> {
    private final ArrowModel model;
    public final ResourceLocation arrowTexture;
    public static final ResourceLocation BODY_TEXTURE = getTextureLocation("arrow_plus");
    public static final ResourceLocation HEAD_TEXTURE = getTextureLocation("arrow_plus_head");

    public ModArrowRenderer(EntityRendererProvider.Context p_174399_, ResourceLocation arrowTexture) {
        super(p_174399_);
        this.arrowTexture = arrowTexture;
        this.model = new ArrowModel(p_174399_.bakeLayer(ModelLayers.ARROW));
    }

    protected ResourceLocation getTextureLocation(ModArrowRenderState p_364566_) {
        return BODY_TEXTURE;
    }

    public ModArrowRenderState createRenderState() {
        return new ModArrowRenderState();
    }

    public static ResourceLocation getTextureLocation(String textureName) {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "textures/entity/projectiles/" + textureName + ".png");
    }

    @Override
    public void render(ModArrowRenderState p_361021_, PoseStack p_113822_, MultiBufferSource p_113823_, int p_113824_) {
        p_113822_.pushPose();
        p_113822_.mulPose(Axis.YP.rotationDegrees(p_361021_.yRot - 90.0F));
        p_113822_.mulPose(Axis.ZP.rotationDegrees(p_361021_.xRot));
        VertexConsumer head = p_113823_.getBuffer(RenderType.entityCutout(HEAD_TEXTURE));
        this.model.setupAnim(p_361021_);
        this.model.renderToBuffer(p_113822_, head, p_113824_, OverlayTexture.NO_OVERLAY, p_361021_.color);
        p_113822_.popPose();
        super.render(p_361021_, p_113822_, p_113823_, p_113824_);
    }

    @Override
    public void extractRenderState(ModArrowEntity arrowEntity, ModArrowRenderState renderState, float p_360538_) {
        super.extractRenderState(arrowEntity, renderState, p_360538_);
        renderState.color = arrowEntity.getColor();
    }
}
