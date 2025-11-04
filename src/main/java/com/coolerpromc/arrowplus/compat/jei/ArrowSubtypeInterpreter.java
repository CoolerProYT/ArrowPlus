package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.util.ArrowData;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class ArrowSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final  ArrowSubtypeInterpreter INSTANCE = new ArrowSubtypeInterpreter();


    public String getStringName(ItemStack itemStack) {
        if (itemStack.getTag() != null && !itemStack.getTag().contains("arrow_data")) {
            return "";
        }

        return itemStack.getTag().getString("arrow_data");
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }
}
