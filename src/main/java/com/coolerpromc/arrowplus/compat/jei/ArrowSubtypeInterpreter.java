package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.util.ArrowData;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class ArrowSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final  ArrowSubtypeInterpreter INSTANCE = new ArrowSubtypeInterpreter();


    public String getStringName(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return "";
        }
        ArrowData arrowData = ArrowData.load(itemStack.getOrCreateTag());
        return arrowData.translationKey();
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }
}
