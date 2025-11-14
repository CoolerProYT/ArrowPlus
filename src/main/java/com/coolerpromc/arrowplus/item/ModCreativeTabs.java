package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModCreativeTabs {
    public static final ItemGroup ARROW_PLUS_TAB = Registry.register(Registries.ITEM_GROUP, Identifier.of(ArrowPlus.MODID, "arrow_plus"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.ARROW))
                    .displayName(Text.translatable("creativetab.arrowplus"))
                    .entries((itemDisplayParameters, output) -> {
                        List<RegistryEntry.Reference<ArrowData>> holder = itemDisplayParameters.lookup().getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).toList();

                        holder.forEach(arrowData -> {
                            ItemStack arrow = ModItems.ARROW_PLUS.getDefaultStack();
                            arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                            output.add(arrow);
                        });

                        holder.forEach(arrowData -> itemDisplayParameters.lookup().getWrapperOrThrow(RegistryKeys.POTION).streamEntries().forEach(potionReference -> {
                            if (!potionReference.value().getEffects().isEmpty()){
                                ItemStack arrow = ModItems.ARROW_PLUS.getDefaultStack();
                                arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                                arrow.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potionReference));
                                output.add(arrow);
                            }
                        }));
                    })
                    .build()
    );

    public static final ItemGroup ARROW_PLUS_MATERIAL_TAB  = Registry.register(Registries.ITEM_GROUP, Identifier.of(ArrowPlus.MODID, "arrow_plus_material"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.GILDED_FEATHER))
                    .displayName(Text.translatable("creativetab.arrowplus.material"))
                    .entries((itemDisplayParameters, output) -> {
                        output.add(ModItems.COPPER_STICK);
                        output.add(ModItems.IRON_STICK);
                        output.add(ModItems.GOLD_STICK);
                        output.add(ModItems.DIAMOND_STICK);
                        output.add(ModItems.EMERALD_STICK);
                        output.add(ModItems.NETHERITE_STICK);
                        output.add(ModItems.GILDED_FEATHER);
                    })
                    .build()
    );


    public static void register() {

    }
}
