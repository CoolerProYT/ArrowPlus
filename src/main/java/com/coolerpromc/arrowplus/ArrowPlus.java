package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ModRecipeSerializer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;

@Mod(ArrowPlus.MODID)
public class ArrowPlus {
    public static final String MODID = "arrowplus";

    public ArrowPlus(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, ArrowPlusConfig.CONFIG_SPEC);
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), ModArrowRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
            event.register(Identifier.fromNamespaceAndPath(MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(MODID, "bow_tint"), BowTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(MODID, "stick_tint"), StickTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);
        }

        @SubscribeEvent
        public static void onRegisterConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
            event.register(Identifier.fromNamespaceAndPath(MODID, "tipped"), TippedCondition.MAP_CODEC);
        }
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
