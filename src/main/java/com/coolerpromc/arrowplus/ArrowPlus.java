package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.entity.renderer.ModArrowRenderer;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ModRecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
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
        public static void onFMLClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(ModItems.ARROW_PLUS.get(), id("tipped"), (itemStack, clientLevel, livingEntity, i) -> {
                    if (itemStack.has(DataComponents.POTION_CONTENTS)){
                        return 1f;
                    }
                    return 0f;
                });
            });
        }

        @SubscribeEvent
        public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ARROW_PLUS.get(), ModArrowRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
            event.register((itemStack, i) -> {
                if (itemStack.getItem() == ModItems.ARROW_PLUS.get()) {
                    Holder<ArrowData> arrowData = itemStack.get(ModDataComponents.ARROW_DATA);
                    if (arrowData != null){
                        ArrowData data = arrowData.value();
                        if (i == 0){
                            if (data.stick().value() instanceof ModStickItem && data.stickData().isPresent()){
                                Holder<StickData> stickData = data.stickData().get();
                                return stickData.value().color();
                            }
                            return 0xFF886627;
                        }
                        if(i == 1){
                            return arrowData.value().color();
                        }
                        if (i == 2){
                            if (data.feather().value() instanceof ModFeatherItem && data.featherData().isPresent()){
                                Holder<FeatherData> featherData = data.featherData().get();
                                return featherData.value().color();
                            }
                        }
                        if (i == 3){
                            if (itemStack.has(DataComponents.POTION_CONTENTS)){
                                PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
                                return contents.getColor();
                            }
                        }
                    }
                }
                return -1;
            }, ModItems.ARROW_PLUS.get());

            event.register((itemStack, i) -> {
                Holder<StickData> stickData = itemStack.get(ModDataComponents.STICK_DATA);
                if (stickData != null){
                    return stickData.value().color();
                }
                return -1;
            }, ModItems.CUSTOM_STICK);

            event.register((itemStack, i) -> {
                Holder<FeatherData> featherData = itemStack.get(ModDataComponents.FEATHER_DATA);
                if (featherData != null){
                    return featherData.value().color();
                }
                return -1;
            }, ModItems.CUSTOM_FEATHER);

            event.register((itemStack, i) -> {
                if (itemStack.is(Items.BOW)){
                    Minecraft minecraft = Minecraft.getInstance();
                    Player player = minecraft.player;
                    if (player != null) {
                        ItemStack arrowStack = player.getProjectile(itemStack);
                        Holder<ArrowData> arrowData = arrowStack.get(ModDataComponents.ARROW_DATA);
                        if (i == 1){
                            if (arrowStack.getItem() == Items.ARROW){
                                return 0xFF141414;
                            }
                            if (arrowData != null){
                                return arrowData.value().color();
                            }
                        }
                        if (i == 2){
                            if (arrowData != null && arrowData.value().stick().value() instanceof ModStickItem && arrowData.value().stickData().isPresent()){
                                return arrowData.value().stickData().get().value().color();
                            }
                            return 0xFF886627;
                        }
                    }
                }
                return -1;
            }, Items.BOW);
        }
    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
