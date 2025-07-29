package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datagen.model.ArrowTintSource;
import com.coolerpromc.arrowplus.datagen.model.BowTintSource;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(ArrowPlus.MODID)
public class ArrowPlus {
    public static final String MODID = "arrowplus";

    public ArrowPlus(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        Registry<ArrowData> registry = event.getServer().registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY);
        ArrowData.VARIANT_STACKS.clear();

        for (ArrowData data : registry){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS.get());
            stack.set(ModDataComponents.ARROW_DATA, data);
            ArrowData.VARIANT_STACKS.add(stack);
        }
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));
        }

        @SubscribeEvent
        public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
            event.register(ResourceLocation.fromNamespaceAndPath(MODID, "arrow_tint"), ArrowTintSource.MAP_CODEC);
            event.register(ResourceLocation.fromNamespaceAndPath(MODID, "bow_tint"), BowTintSource.MAP_CODEC);
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            Registry<ArrowData> registry = event.getEntity().level().registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY);
            ArrowData.VARIANT_STACKS.clear();

            for (ArrowData data : registry) {
                ItemStack stack = new ItemStack(ModItems.ARROW_PLUS.get());
                stack.set(ModDataComponents.ARROW_DATA, data);
                ArrowData.VARIANT_STACKS.add(stack);
            }
        }
    }
}
