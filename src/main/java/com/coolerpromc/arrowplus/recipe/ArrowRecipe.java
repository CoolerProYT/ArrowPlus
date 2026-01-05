package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class ArrowRecipe extends CustomRecipe {
    public ArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.width() == 1 && craftingInput.height() == 3 && craftingInput.ingredientCount() == 3){
            List<Holder.Reference<ArrowData>> arrowDataList = level.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).toList();

            ItemStack materialStack = craftingInput.getItem(0);
            ItemStack stickStack = craftingInput.getItem(1);
            ItemStack featherStack = craftingInput.getItem(2);

            for (Holder<ArrowData> arrowData : arrowDataList){
                if (arrowData.value().isValidMaterial(materialStack, stickStack, featherStack)){
                    return true;
                }
            }
            return false;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        List<Either<Holder<Item>, TagKey<Item>>> materialList = new ArrayList<>();
        Holder<ArrowData> arrowData = null;
        ItemStack materialStack = craftingInput.getItem(0);

        List<Holder.Reference<ArrowData>> arrowDataList = provider.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).toList();
        for (Holder<ArrowData> holder : arrowDataList){
            materialList.add(holder.value().material());
            if (holder.value().material().left().isPresent() && materialStack.is(holder.value().material().left().get())) {
                arrowData = holder;
                break;
            }
            else if (holder.value().material().right().isPresent() && materialStack.is(holder.value().material().right().get())) {
                arrowData = holder;
                break;
            }
        }

        if (arrowData != null && materialList.contains(arrowData.value().material())){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS.get(), arrowData.value().outputAmount());
            stack.set(ModDataComponents.ARROW_DATA.get(), arrowData);
            return stack;
        }
        else{
            return ItemStack.EMPTY;
        }
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializer.ARROW_RECIPE_SERIALIZER.get();
    }
}
