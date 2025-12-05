package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(ArrowPlus.MODID)
public class ArrowPlus {
    public static final String MODID = "arrowplus";

    public ArrowPlus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArrowPlusConfig.CONFIG_SPEC);

        MinecraftForge.EVENT_BUS.addListener(this::onLivingGetProjectile);
    }

    @SubscribeEvent
    public void onLivingGetProjectile(LivingGetProjectileEvent event) {
        if (event.getProjectileItemStack().is(Items.ARROW)){
            event.setProjectileItemStack(new ItemStack(event.getProjectileItemStack().getItem(), event.getProjectileItemStack().getCount()));
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), context -> new ModArrowRenderer(context, ModArrowRenderer.getTextureLocation("arrow_plus")));
        }

        @SubscribeEvent
        public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
            event.register((itemStack, i) -> {
                if (itemStack.getItem() == ModItems.ARROW_PLUS.get() && Minecraft.getInstance().level != null) {
                    ArrowData arrowData = ArrowData.load(itemStack.getOrCreateTag(), Minecraft.getInstance().level.registryAccess());
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
                    if (player != null && Minecraft.getInstance().level != null) {
                        ItemStack projectile = player.getProjectile(itemStack);
                        CompoundTag projectileTag = projectile.getOrCreateTag();

                        ArrowData arrowData = ArrowData.load(projectileTag, Minecraft.getInstance().level.registryAccess());

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
