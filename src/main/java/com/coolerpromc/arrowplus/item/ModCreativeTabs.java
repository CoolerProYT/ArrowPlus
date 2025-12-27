package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArrowPlus.MODID);

    public static final RegistryObject<CreativeModeTab> ARROW_PLUS_TAB = CREATIVE_MOD_TABS.register("arrow_plus",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ARROW))
                    .title(Component.translatable("creativetab.arrowplus"))
                    .displayItems((itemDisplayParameters, output) -> {
                        List<Holder.Reference<ArrowData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).toList();
                        holder.forEach(arrowData -> {
                            ItemStack arrow = ModItems.ARROW_PLUS.get().getDefaultInstance();
                            arrow.set(ModDataComponents.ARROW_DATA.get(), arrowData);
                            output.accept(arrow);
                        });

                        holder.forEach(arrowData -> itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potionReference -> {
                            if (!potionReference.value().getEffects().isEmpty()){
                                ItemStack arrow = ModItems.ARROW_PLUS.get().getDefaultInstance();
                                arrow.set(ModDataComponents.ARROW_DATA.get(), arrowData);
                                arrow.set(DataComponents.POTION_CONTENTS, new PotionContents(potionReference));
                                output.accept(arrow);
                            }
                        }));
                    })
                    .build()
    );

    public static final RegistryObject<CreativeModeTab> ARROW_PLUS_MATERIAL_TAB = CREATIVE_MOD_TABS.register("arrow_plus_material",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GILDED_FEATHER.get()))
                    .title(Component.translatable("creativetab.arrowplus.material"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.COPPER_STICK.get());
                        output.accept(ModItems.IRON_STICK.get());
                        output.accept(ModItems.GOLD_STICK.get());
                        output.accept(ModItems.DIAMOND_STICK.get());
                        output.accept(ModItems.EMERALD_STICK.get());
                        output.accept(ModItems.NETHERITE_STICK.get());
                        output.accept(ModItems.GILDED_FEATHER.get());
                    })
                    .build()
    );

    public static void register(BusGroup eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
