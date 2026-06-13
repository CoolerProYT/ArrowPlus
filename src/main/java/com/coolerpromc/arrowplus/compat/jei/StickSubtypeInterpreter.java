package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StickSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final StickSubtypeInterpreter INSTANCE = new StickSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ModDataComponents.STICK_DATA);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }

    public String getStringName(ItemStack itemStack) {
        if (itemStack.getComponentsPatch().isEmpty()) {
            return "";
        }
        StickData stickData = itemStack.getOrDefault(ModDataComponents.STICK_DATA, Holder.direct(StickData.EMPTY)).value();
        return stickData.translationKey();
    }
}
