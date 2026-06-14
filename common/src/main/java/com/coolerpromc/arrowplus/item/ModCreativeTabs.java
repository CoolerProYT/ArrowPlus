package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.platform.Services;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;

public class ModCreativeTabs {
    public static final RegistryHandler<CreativeModeTab, CreativeModeTab> ARROW_PLUS_TAB = Services.REGISTRY.registerCreativeTab("arrow_plus", () -> new ItemStack(Items.ARROW), Component.translatable("creativetab.arrowplus"),
            (itemDisplayParameters) -> {
                List<ItemStack> stacks = new ArrayList<>();
                List<Holder.Reference<ArrowData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.getRemoval().contains(reference.key().identifier().getPath())).toList();
                holder.forEach(arrowData -> {
                    ItemStack arrow = ModItems.ARROW_PLUS.toStack();
                    arrow.set(ModDataComponents.ARROW_DATA.get(), arrowData);
                    stacks.add(arrow);
                });

                holder.forEach(arrowData -> itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potionReference -> {
                    if (!potionReference.value().getEffects().isEmpty()){
                        ItemStack arrow = ModItems.ARROW_PLUS.toStack();
                        arrow.set(ModDataComponents.ARROW_DATA.get(), arrowData);
                        arrow.set(DataComponents.POTION_CONTENTS, new PotionContents(potionReference));
                        stacks.add(arrow);
                    }
                }));
                return stacks.toArray(new ItemStack[0]);
            }
    );

    public static final RegistryHandler<CreativeModeTab, CreativeModeTab> ARROW_PLUS_MATERIAL_TAB = Services.REGISTRY.registerCreativeTab("arrow_plus_material", () -> new ItemStack(ModItems.GILDED_FEATHER.get()), Component.translatable("creativetab.arrowplus.material"),
            (itemDisplayParameters) -> {
                List<Holder.Reference<StickData>> stickHolder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().toList();
                List<ItemStack> stacks = new ArrayList<>();
                stickHolder.forEach(stickData -> {
                    ItemStack stick = ModItems.CUSTOM_STICK.toStack();
                    stick.set(ModDataComponents.STICK_DATA.get(), stickData);
                    stacks.add(stick);
                });

                List<Holder.Reference<FeatherData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().toList();
                holder.forEach(featherData -> {
                    ItemStack feather = ModItems.CUSTOM_FEATHER.toStack();
                    feather.set(ModDataComponents.FEATHER_DATA.get(), featherData);
                    stacks.add(feather);
                });

                return stacks.toArray(new ItemStack[0]);
            }
    );

    public static void load() {
    }
}
