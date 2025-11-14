package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class TippedArrowRecipe extends SpecialCraftingRecipe {
    public TippedArrowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    public boolean matches(CraftingRecipeInput craftingInput, World level) {
        if (craftingInput.getWidth() == 3 && craftingInput.getHeight() == 3 && craftingInput.getStackCount() == 9) {
            for(int i = 0; i < craftingInput.getHeight(); ++i) {
                for(int j = 0; j < craftingInput.getWidth(); ++j) {
                    ItemStack itemstack = craftingInput.getStackInSlot(j, i);
                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    if (j == 1 && i == 1) {
                        if (!itemstack.isOf(Items.LINGERING_POTION)) {
                            return false;
                        }
                    } else if (!itemstack.isOf(ModItems.ARROW_PLUS)) {
                        return false;
                    }
                    else if(itemstack.isOf(ModItems.ARROW_PLUS) && itemstack.contains(DataComponentTypes.POTION_CONTENTS)){
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack craft(CraftingRecipeInput craftingInput, RegistryWrapper.WrapperLookup provider) {
        ItemStack itemstack = craftingInput.getStackInSlot(1, 1);
        if (!itemstack.isOf(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = craftingInput.getStackInSlot(0, 0).copyWithCount(8);
            itemstack1.set(DataComponentTypes.POTION_CONTENTS, itemstack.get(DataComponentTypes.POTION_CONTENTS));
            return itemstack1;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    public RecipeSerializer<TippedArrowRecipe> getSerializer() {
        return ModRecipeSerializer.TIPPED_ARROW_RECIPE_SERIALIZER;
    }
}