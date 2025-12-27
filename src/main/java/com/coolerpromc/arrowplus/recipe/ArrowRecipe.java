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
            List<Either<Holder<Item>, TagKey<Item>>> materialList = new ArrayList<>();

            List<Holder.Reference<ArrowData>> arrowDataList = level.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).toList();
            arrowDataList.forEach(arrowData -> materialList.add(arrowData.value().material()));

            boolean hasStick = false;
            boolean hasFeather = false;

            Item materialItem = null;
            TagKey<Item> materialTag = null;

            ItemStack materialStack = craftingInput.getItem(0);

            for (Either<Holder<Item>, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && materialStack.is(material.left().get())) {
                    materialItem = materialStack.getItem();
                    break;
                } else if (material.right().isPresent() && materialStack.is(material.right().get())) {
                    materialTag = material.right().get();
                    break;
                }
            }

            Item stick = Items.AIR;
            Item feather = Items.AIR;
            for (Holder<ArrowData> arrowData : arrowDataList){
                if (materialItem != null && arrowData.value().material().left().orElseThrow().is(materialItem.builtInRegistryHolder())){
                    stick = arrowData.value().stick().value();
                    feather = arrowData.value().feather().value();
                    break;
                }
                else if (materialTag != null && arrowData.value().material().right().orElseThrow().equals(materialTag)){
                    stick = arrowData.value().stick().value();
                    feather = arrowData.value().feather().value();
                    break;
                }
            }

            if (stick == Items.AIR || feather == Items.AIR) return false;

            if (craftingInput.getItem(1).is(stick)){
                hasStick = true;
            }

            if (craftingInput.getItem(2).is(feather)){
                hasFeather = true;
            }

            return hasStick && hasFeather;
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

        List<Holder.Reference<ArrowData>> arrowDataList = provider.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).toList();
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
            stack.set(ModDataComponents.ARROW_DATA, arrowData);
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
