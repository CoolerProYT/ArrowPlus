package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.item.ModItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.forge.REIPluginClient;

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
}
