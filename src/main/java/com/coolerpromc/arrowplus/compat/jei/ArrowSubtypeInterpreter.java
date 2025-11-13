package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.util.ArrowData;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ArrowSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final  ArrowSubtypeInterpreter INSTANCE = new ArrowSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ModDataComponents.ARROW_DATA);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }

    public String getStringName(ItemStack itemStack) {
        if (itemStack.getComponentsPatch().isEmpty()) {
            return "";
        }
        ArrowData arrowData = itemStack.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value();
        return arrowData.translationKey();
    }
}
