package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
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
        if (itemStack.getComponentChanges().isEmpty()) {
            return "";
        }
        ArrowData arrowData = itemStack.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value();
        PotionContentsComponent potionContents = itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
        if (potionContents.potion().isPresent()){
            return arrowData.translationKey() + potionContents.potion().get().getKey().get().getValue().getPath();
        }
        return arrowData.translationKey();
    }
}