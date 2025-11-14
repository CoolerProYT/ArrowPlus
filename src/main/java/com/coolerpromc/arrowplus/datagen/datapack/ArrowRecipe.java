package com.coolerpromc.arrowplus.datagen.datapack;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import com.mojang.datafixers.util.Either;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArrowRecipe extends SpecialCraftingRecipe {
    public ArrowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput craftingInput, World level) {
        if (craftingInput.getWidth() == 1 && craftingInput.getHeight() == 3 && craftingInput.getStackCount() == 3){
            List<Either<Identifier, TagKey<Item>>> materialList = new ArrayList<>();

            level.getRegistryManager().getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).forEach(arrowData -> {
                materialList.add(arrowData.value().material());
            });

            boolean hasMaterial = false;
            boolean hasStick = false;
            boolean hasFeather = false;

            ItemStack materialStack = craftingInput.getStackInSlot(0);

            for (Either<Identifier, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && material.left().get().equals(Registries.ITEM.getId(materialStack.getItem()))) {
                    hasMaterial = true;
                    break;
                } else if (material.right().isPresent() && materialStack.isIn(material.right().get())) {
                    hasMaterial = true;
                    break;
                }
            }

            if (craftingInput.getStackInSlot(1).isOf(Items.STICK)){
                hasStick = true;
            }

            if (craftingInput.getStackInSlot(2).isOf(Items.FEATHER)){
                hasFeather = true;
            }

            return hasMaterial && hasStick && hasFeather;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingInput, RegistryWrapper.WrapperLookup provider) {
        List<Either<Identifier, TagKey<Item>>> materialList = new ArrayList<>();
        AtomicReference<RegistryEntry<ArrowData>> arrowData = new AtomicReference<>();
        ItemStack materialStack = craftingInput.getStackInSlot(0);

        provider.getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).forEach(holder ->{
            materialList.add(holder.value().material());
            if (holder.value().material().left().isPresent() && holder.value().material().left().get().equals(Registries.ITEM.getId(materialStack.getItem()))) {
                arrowData.set(holder);
            }
            else if (holder.value().material().right().isPresent() && materialStack.isIn(holder.value().material().right().get())) {
                arrowData.set(holder);
            }
        });

        if (materialList.contains(arrowData.get().value().material())){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS, 4);
            stack.set(ModDataComponents.ARROW_DATA, arrowData.get());
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
