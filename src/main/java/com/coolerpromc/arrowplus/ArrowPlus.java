package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

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

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));
        }

        @SubscribeEvent
        public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
            event.register((itemStack, i) -> {
                if (itemStack.getItem() == ModItems.ARROW_PLUS.get()) {
                    Holder<ArrowData> arrowData = itemStack.get(ModDataComponents.ARROW_DATA);
                    if (arrowData != null && i == 1){
                        return arrowData.value().color();
                    }
                }
                return -1;
            }, ModItems.ARROW_PLUS.get());

            event.register((itemStack, i) -> {
                if (itemStack.is(Items.BOW)){
                    Minecraft minecraft = Minecraft.getInstance();
                    Player player = minecraft.player;
                    if (player != null) {
                        Holder<ArrowData> arrowData = player.getProjectile(itemStack).get(ModDataComponents.ARROW_DATA);
                        if (arrowData != null && i == 1) {
                            return arrowData.value().color();
                        }
                    }
                }
                return -1;
            }, Items.BOW);
        }
    }
}
