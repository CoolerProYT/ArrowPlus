package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class ArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));

        ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> {
            if (itemStack.getItem() == ModItems.ARROW_PLUS) {
                ArrowData arrowData = ArrowData.load(itemStack.getOrCreateNbt());
                if (arrowData != null && tintIndex == 1){
                    return arrowData.color();
                }
            }
            return -1;
        }, ModItems.ARROW_PLUS);

        ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> {
            if (itemStack.isOf(Items.BOW)){
                MinecraftClient minecraft = MinecraftClient.getInstance();
                PlayerEntity player = minecraft.player;
                if (player != null) {
                    ArrowData arrowData = ArrowData.load(player.getProjectileType(itemStack).getOrCreateNbt());
                    if (arrowData != null && tintIndex == 1) {
                        return arrowData.color();
                    }
                }
            }
            return -1;
        }, Items.BOW);
    }
}
