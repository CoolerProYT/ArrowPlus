package com.coolerpromc.arrowplus.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class ModREIPluginClient implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ArrowRecipeFiller.get().forEach(registry::add);
    }
}
