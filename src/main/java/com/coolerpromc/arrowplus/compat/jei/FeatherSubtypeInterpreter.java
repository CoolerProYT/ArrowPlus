package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

public class FeatherSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final FeatherSubtypeInterpreter INSTANCE = new FeatherSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ModDataComponents.FEATHER_DATA);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }

    public String getStringName(ItemStack itemStack) {
        if (itemStack.getComponentsPatch().isEmpty()) {
            return "";
        }
        FeatherData featherData = itemStack.getOrDefault(ModDataComponents.FEATHER_DATA, Holder.direct(FeatherData.EMPTY)).value();
        return featherData.translationKey();
    }
}
