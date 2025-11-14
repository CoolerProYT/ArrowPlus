package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public class ArrowPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, ModArrowRenderer::new);

        ModelPredicateProviderRegistry.register(ModItems.ARROW_PLUS, ArrowPlus.id("tipped"), (stack, world, entity, seed) -> stack.contains(DataComponentTypes.POTION_CONTENTS) ? 1.0F : 0.0F);

        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
            if (itemStack.getItem() == ModItems.ARROW_PLUS) {
                RegistryEntry<ArrowData> arrowData = itemStack.get(ModDataComponents.ARROW_DATA);
                if (arrowData != null){
                    ArrowData data = arrowData.value();
                    if (i == 0){
                        if(data.stick().value() instanceof ModStickItem item){
                            return item.getColor();
                        }
                        return 0xFF886627;
                    }
                    if(i == 1){
                        return arrowData.value().color();
                    }
                    if (i == 2){
                        if (data.feather().value() instanceof ModFeatherItem item){
                            return item.getColor();
                        }
                    }
                    if (i == 3){
                        if (itemStack.contains(DataComponentTypes.POTION_CONTENTS)){
                            PotionContentsComponent contents = itemStack.get(DataComponentTypes.POTION_CONTENTS);
                            return contents.getColor();
                        }
                    }
                }
            }
            return -1;
        }, ModItems.ARROW_PLUS);

        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
            if (itemStack.getItem() instanceof ModStickItem stickItem){
                return stickItem.getColor();
            }
            return -1;
        }, ModItems.COPPER_STICK, ModItems.IRON_STICK, ModItems.GOLD_STICK, ModItems.DIAMOND_STICK, ModItems.EMERALD_STICK, ModItems.NETHERITE_STICK);

        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
            if (itemStack.getItem() instanceof ModFeatherItem featherItem){
                return featherItem.getColor();
            }
            return -1;
        }, ModItems.GILDED_FEATHER);

        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
            if (itemStack.isOf(Items.BOW)){
                MinecraftClient minecraft = MinecraftClient.getInstance();
                PlayerEntity player = minecraft.player;
                if (player != null) {
                    ItemStack arrowStack = player.getProjectileType(itemStack);
                    RegistryEntry<ArrowData> arrowData = arrowStack.get(ModDataComponents.ARROW_DATA);
                    if (i == 1){
                        if (arrowStack.getItem() == Items.ARROW){
                            return 0xFF141414;
                        }
                        if (arrowData != null){
                            return arrowData.value().color();
                        }
                    }
                    if (i == 2){
                        if (arrowData != null && arrowData.value().stick().value() instanceof ModStickItem item){
                            return item.getColor();
                        }
                        return 0xFF886627;
                    }
                }
            }
            return -1;
        }, Items.BOW);

        EntityRendererRegistry.register(ModEntities.ARROW_PLUS, ModArrowRenderer::new);
    }
}
