package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TippedArrowRecipe extends CustomRecipe {
    public static final TippedArrowRecipe INSTANCE = new TippedArrowRecipe();
    public static final MapCodec<TippedArrowRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, TippedArrowRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<TippedArrowRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

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
                    } else if (!itemstack.is(ModItems.ARROW_PLUS)) {
                        return false;
                    }
                    else if(itemstack.is(ModItems.ARROW_PLUS) && itemstack.has(DataComponents.POTION_CONTENTS)){
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingInput craftingInput) {
        ItemStack itemstack = craftingInput.getItem(1, 1);
        if (!itemstack.is(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = craftingInput.getItem(0, 0).copyWithCount(8);
            itemstack1.set(DataComponents.POTION_CONTENTS, itemstack.get(DataComponents.POTION_CONTENTS));
            return itemstack1;
        }
    }

    public RecipeSerializer<TippedArrowRecipe> getSerializer() {
        return ModRecipeSerializer.TIPPED_ARROW_RECIPE_SERIALIZER;
    }
}