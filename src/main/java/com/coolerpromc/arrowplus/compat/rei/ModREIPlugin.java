package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

@REIPluginClient
public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        new ArrowRecipeFiller().registerDisplays(registry);
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerComponents(ModItems.ARROW_PLUS.get());
    }

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        if (ArrowPlusConfig.CONFIG.hideTippedArrow()){
            BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).forEach(data -> {
                ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get());
                output.set(ModDataComponents.ARROW_DATA, data);

                BasicDisplay.registryAccess().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                    if (!potion.value().getEffects().isEmpty()) {
                        ItemStack arrow = output.copyWithCount(1);
                        ItemStack tippedOutput = arrow.copy();
                        tippedOutput.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                        rule.hide(EntryStacks.of(tippedOutput));
                    }
                });
            });
        }
    }
}
