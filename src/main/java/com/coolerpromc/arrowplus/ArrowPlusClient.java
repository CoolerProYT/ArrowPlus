package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datagen.model.ArrowTintSource;
import com.coolerpromc.arrowplus.datagen.model.BowTintSource;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;

public class ArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ArrowPlus.MODID, "bow_tint"), BowTintSource.MAP_CODEC);

        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));
    }
}
