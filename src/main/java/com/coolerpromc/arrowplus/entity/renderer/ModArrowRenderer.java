package com.coolerpromc.arrowplus.entity.renderer;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
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
    public void render(ModArrowRenderState p_361021_, MatrixStack p_113822_, VertexConsumerProvider p_113823_, int p_113824_) {
        p_113822_.push();
        p_113822_.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(p_361021_.yaw - 90.0F));
        p_113822_.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(p_361021_.pitch));
        VertexConsumer head = p_113823_.getBuffer(RenderLayer.getEntityCutout(HEAD_TEXTURE));
        this.model.setAngles(p_361021_);
        this.model.render(p_113822_, head, p_113824_, OverlayTexture.DEFAULT_UV, p_361021_.color);
        p_113822_.pop();
        super.render(p_361021_, p_113822_, p_113823_, p_113824_);
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
