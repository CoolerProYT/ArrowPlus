package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;

public class ArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "bow_tint"), BowTintSource.MAP_CODEC);
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "stick_tint"), StickTintSource.MAP_CODEC);
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);

        BooleanProperties.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "tipped"), TippedCondition.MAP_CODEC);

        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, ModArrowRenderer::new);
    }
}
