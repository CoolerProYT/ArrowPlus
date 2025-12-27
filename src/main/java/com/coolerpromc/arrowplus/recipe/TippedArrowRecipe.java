package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TippedArrowRecipe extends CustomRecipe {
    public TippedArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.width() == 3 && craftingInput.height() == 3 && craftingInput.ingredientCount() == 9) {
            for(int i = 0; i < craftingInput.height(); ++i) {
                for(int j = 0; j < craftingInput.width(); ++j) {
                    ItemStack itemstack = craftingInput.getItem(j, i);
                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    if (j == 1 && i == 1) {
                        if (!itemstack.is(Items.LINGERING_POTION)) {
                            return false;
                        }
                    } else if (!itemstack.is(ModItems.ARROW_PLUS.get())) {
                        return false;
                    }
                    else if(itemstack.is(ModItems.ARROW_PLUS.get()) && itemstack.has(DataComponents.POTION_CONTENTS)){
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack itemstack = craftingInput.getItem(1, 1);
        if (!itemstack.is(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = craftingInput.getItem(0, 0).copyWithCount(8);
            itemstack1.set(DataComponents.POTION_CONTENTS, itemstack.get(DataComponents.POTION_CONTENTS));
            return itemstack1;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    public RecipeSerializer<TippedArrowRecipe> getSerializer() {
        return ModRecipeSerializer.TIPPED_ARROW_RECIPE_SERIALIZER.get();
    }
}
