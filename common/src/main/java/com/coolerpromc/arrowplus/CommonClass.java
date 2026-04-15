package com.coolerpromc.arrowplus;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.ModCreativeTabs;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ModRecipeSerializer;

public class CommonClass {
    public static void init() {
        ModDataComponents.load();
        ModItems.load();
        ModEntities.load();
        ModCreativeTabs.load();
        ModRecipeSerializer.load();
    }
}