package com.coolerpromc.arrowplus.datapack.feather;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

public class Feathers {
    public static final ResourceKey<FeatherData> GILDED = key("gilded");

    public static void bootstrap(BootstrapContext<FeatherData> context) {
        context.register(GILDED, new FeatherData(Items.GLOWSTONE_DUST, 0xFFfffc69, "item.arrowplus.gilded_feather"));
    }

    private static ResourceKey<FeatherData> key(String name){
        return ResourceKey.create(ModRegistries.FEATHER_DATA_KEY, ArrowPlus.id(name));
    }
}
