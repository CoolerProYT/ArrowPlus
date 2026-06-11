package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ModRecipeSerializer;

public class ArrowPlus {
    public static void init() {
        ArrowPlusConfig.init();

        ModDataComponents.load();
        ModItems.load();
        ModEntities.load();
        ModCreativeTabs.load();
        ModRecipeSerializer.load();
    }
}