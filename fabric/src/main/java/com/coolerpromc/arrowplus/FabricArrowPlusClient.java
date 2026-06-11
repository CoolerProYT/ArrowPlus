package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.client.item.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;

public class FabricArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "bow_tint"), BowTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "stick_tint"), StickTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);

        ConditionalItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID, "tipped"), TippedCondition.MAP_CODEC);

        EntityRenderers.register(ModEntities.ARROW_PLUS.get(), ModArrowRenderer::new);
    }
}
