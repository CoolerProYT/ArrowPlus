package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ModRecipeSerializer;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArrowPlus.MODID)
public class ArrowPlus {
    public static final String MODID = "arrowplus";

    public ArrowPlus(FMLJavaModLoadingContext context) {
        BusGroup modEventBus = context.getModBusGroup();

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);

        context.registerConfig(ModConfig.Type.COMMON, ArrowPlusConfig.CONFIG_SPEC);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), ModArrowRenderer::new);

            ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
            ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "bow_tint"), BowTintSource.MAP_CODEC);
            ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "stick_tint"), StickTintSource.MAP_CODEC);
            ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "bow_stick_tint"), BowStickTintSource.MAP_CODEC);
            ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "feather_tint"), FeatherTintSource.MAP_CODEC);

            ConditionalItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "tipped"), TippedCondition.MAP_CODEC);
        }
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
