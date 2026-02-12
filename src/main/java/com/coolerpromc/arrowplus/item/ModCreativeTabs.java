package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import java.util.List;

public class ModCreativeTabs {
    public static final CreativeModeTab ARROW_PLUS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_plus"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(Items.ARROW))
                    .title(Component.translatable("creativetab.arrowplus"))
                    .displayItems((itemDisplayParameters, output) -> {
                        List<Holder.Reference<ArrowData>> holder = itemDisplayParameters.holders().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).toList();

                        holder.forEach(arrowData -> {
                            ItemStack arrow = ModItems.ARROW_PLUS.getDefaultInstance();
                            arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                            output.accept(arrow);
                        });

                        holder.forEach(arrowData -> itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potionReference -> {
                            if (!potionReference.value().getEffects().isEmpty()){
                                ItemStack arrow = ModItems.ARROW_PLUS.getDefaultInstance();
                                arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                                arrow.set(DataComponents.POTION_CONTENTS, new PotionContents(potionReference));
                                output.accept(arrow);
                            }
                        }));
                    })
                    .build()
    );

    public static final CreativeModeTab ARROW_PLUS_MATERIAL_TAB  = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_plus_material"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GILDED_FEATHER))
                    .title(Component.translatable("creativetab.arrowplus.material"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.COPPER_STICK);
                        output.accept(ModItems.IRON_STICK);
                        output.accept(ModItems.GOLD_STICK);
                        output.accept(ModItems.DIAMOND_STICK);
                        output.accept(ModItems.EMERALD_STICK);
                        output.accept(ModItems.NETHERITE_STICK);
                        output.accept(ModItems.GILDED_FEATHER);
                    })
                    .build()
    );


    public static void register() {

    }
}
