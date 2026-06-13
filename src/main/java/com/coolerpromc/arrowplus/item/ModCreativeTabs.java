package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArrowPlus.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARROW_PLUS_TAB = CREATIVE_MOD_TABS.register("arrow_plus",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ARROW))
                    .title(Component.translatable("creativetab.arrowplus"))
                    .displayItems((itemDisplayParameters, output) -> {
                        List<Holder.Reference<ArrowData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).toList();
                        holder.forEach(arrowData -> {
                            ItemStack arrow = ModItems.ARROW_PLUS.toStack();
                            arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                            output.accept(arrow);
                        });

                        holder.forEach(arrowData -> itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potionReference -> {
                            if (!potionReference.value().getEffects().isEmpty()){
                                ItemStack arrow = ModItems.ARROW_PLUS.toStack();
                                arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                                arrow.set(DataComponents.POTION_CONTENTS, new PotionContents(potionReference));
                                output.accept(arrow);
                            }
                        }));
                    })
                    .build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARROW_PLUS_MATERIAL_TAB = CREATIVE_MOD_TABS.register("arrow_plus_material",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CUSTOM_FEATHER.get()))
                    .title(Component.translatable("creativetab.arrowplus.material"))
                    .displayItems((itemDisplayParameters, output) -> {
                        List<Holder.Reference<StickData>> stickHolder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().toList();
                        stickHolder.forEach(stickData -> {
                            ItemStack stick = ModItems.CUSTOM_STICK.toStack();
                            stick.set(ModDataComponents.STICK_DATA, stickData);
                            output.accept(stick);
                        });

                        List<Holder.Reference<FeatherData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().toList();
                        holder.forEach(featherData -> {
                            ItemStack feather = ModItems.CUSTOM_FEATHER.toStack();
                            feather.set(ModDataComponents.FEATHER_DATA, featherData);
                            output.accept(feather);
                        });

                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
