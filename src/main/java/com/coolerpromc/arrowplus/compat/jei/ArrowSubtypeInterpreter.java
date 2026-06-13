package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

public class ArrowSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final ArrowSubtypeInterpreter INSTANCE = new ArrowSubtypeInterpreter();

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
        PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potionContents.potion().isPresent()){
            return arrowData.translationKey() + potionContents.potion().get().getKey().location().getPath();
        }
        return arrowData.translationKey();
    }
}
