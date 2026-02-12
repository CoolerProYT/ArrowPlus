package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;

public class ArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "bow_tint"), BowTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "stick_tint"), StickTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);

        ConditionalItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "tipped"), TippedCondition.MAP_CODEC);

        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, ModArrowRenderer::new);
    }
}
