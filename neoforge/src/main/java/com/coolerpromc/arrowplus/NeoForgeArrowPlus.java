package com.coolerpromc.arrowplus;


import com.coolerpromc.arrowplus.client.item.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.platform.NeoForgeRegistryHelper;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;

@Mod(Constants.MODID)
public class NeoForgeArrowPlus {

    public NeoForgeArrowPlus(IEventBus modEventBus, ModContainer modContainer) {
        ArrowPlus.init();

        NeoForgeRegistryHelper.register(modEventBus);
    }

    @EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), ModArrowRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "bow_tint"), BowTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "stick_tint"), StickTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);
        }

        @SubscribeEvent
        public static void onRegisterConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
            event.register(Identifier.fromNamespaceAndPath(Constants.MODID, "tipped"), TippedCondition.MAP_CODEC);
        }
    }
}