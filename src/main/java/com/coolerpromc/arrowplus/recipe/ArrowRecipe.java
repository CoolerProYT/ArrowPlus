package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.datafixers.util.Either;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ArrowRecipe extends SpecialCraftingRecipe {
    public ArrowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput craftingInput, World level) {
        if (craftingInput.getWidth() == 1 && craftingInput.getHeight() == 3 && craftingInput.getStackCount() == 3){
            List<Either<RegistryEntry<Item>, TagKey<Item>>> materialList = new ArrayList<>();

            List<RegistryEntry.Reference<ArrowData>> arrowDataList = level.getRegistryManager().getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).toList();
            arrowDataList.forEach(arrowData -> materialList.add(arrowData.value().material()));

            boolean hasStick = false;
            boolean hasFeather = false;

            Item materialItem = null;
            TagKey<Item> materialTag = null;

            ItemStack materialStack = craftingInput.getStackInSlot(0);

            for (Either<RegistryEntry<Item>, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && materialStack.itemMatches(material.left().get())) {
                    materialItem = materialStack.getItem();
                    break;
                } else if (material.right().isPresent() && materialStack.isIn(material.right().get())) {
                    materialTag = material.right().get();
                    break;
                }
            }

            Item stick = Items.AIR;
            Item feather = Items.AIR;
            for (RegistryEntry<ArrowData> arrowData : arrowDataList){
                if (materialItem != null && arrowData.value().material().left().orElseThrow().matches(materialItem.getRegistryEntry())){
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

            if (craftingInput.getStackInSlot(1).isOf(stick)){
                hasStick = true;
            }

            if (craftingInput.getStackInSlot(2).isOf(feather)){
                hasFeather = true;
            }
            return hasStick && hasFeather;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingInput, RegistryWrapper.WrapperLookup provider) {
        List<Either<RegistryEntry<Item>, TagKey<Item>>> materialList = new ArrayList<>();
        RegistryEntry<ArrowData> arrowData = null;
        ItemStack materialStack = craftingInput.getStackInSlot(0);

        List<RegistryEntry.Reference<ArrowData>> arrowDataList = provider.getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).toList();
        for (RegistryEntry<ArrowData> holder : arrowDataList){
            materialList.add(holder.value().material());
            if (holder.value().material().left().isPresent() && materialStack.itemMatches(holder.value().material().left().get())) {
                arrowData = holder;
            }
            else if (holder.value().material().right().isPresent() && materialStack.isIn(holder.value().material().right().get())) {
                arrowData = holder;
            }
        }

        if (arrowData != null && materialList.contains(arrowData.value().material())){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS, arrowData.value().outputAmount());
            stack.set(ModDataComponents.ARROW_DATA, arrowData);
            return stack;
        }
        else{
            return ItemStack.EMPTY;
        }
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializer.ARROW_RECIPE_SERIALIZER;
    }
}
