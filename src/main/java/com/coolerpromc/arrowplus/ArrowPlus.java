package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArrowPlus.MODID)
public class ArrowPlus {
    public static final String MODID = "arrowplus";

    public ArrowPlus(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));
        }

        @SubscribeEvent
        public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.Item event) {
            event.register((itemStack, i) -> {
                if (itemStack.getItem() == ModItems.ARROW_PLUS.get()) {
                    ArrowData arrowData = itemStack.get(ModDataComponents.ARROW_DATA.get());
                    if (arrowData != null && i == 1){
                        return arrowData.color();
                    }
                }
                return -1;
            }, ModItems.ARROW_PLUS.get());

            event.register((itemStack, i) -> {
                if (itemStack.is(Items.BOW)){
                    Minecraft minecraft = Minecraft.getInstance();
                    Player player = minecraft.player;
                    if (player != null) {
                        ArrowData arrowData = player.getProjectile(itemStack).get(ModDataComponents.ARROW_DATA.get());
                        if (arrowData != null && i == 1) {
                            return arrowData.color();
                        }
                    }
                }
                return -1;
            }, Items.BOW);
        }
    }
}
