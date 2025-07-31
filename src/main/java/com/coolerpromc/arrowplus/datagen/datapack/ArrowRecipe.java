package com.coolerpromc.arrowplus.datagen.datapack;

import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import com.mojang.datafixers.util.Either;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArrowRecipe extends SpecialCraftingRecipe {
    public ArrowRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInput, World level) {
        if (craftingInput.getWidth() == 3 && craftingInput.getHeight() == 3){
            Registry<ArrowData> registry = level.getRegistryManager().get(ModRegistries.ARROW_DATA_KEY);
            List<Either<Identifier, TagKey<Item>>> materialList = new ArrayList<>();
            for (ArrowData data : registry) {
                materialList.add(data.material());
            }

            boolean hasMaterial = false;
            boolean hasStick = false;
            boolean hasFeather = false;

            List<ItemStack> row1 = List.of(craftingInput.getStack(0), craftingInput.getStack(1), craftingInput.getStack(2));

            if (row1.stream().filter(itemStack -> !itemStack.isOf(Items.AIR)).count() != 1){
                return false;
            }
            int column = 0;
            for (ItemStack itemStack : row1) {
                if (!itemStack.isOf(Items.AIR)) {
                    break;
                }
                column++;
            }

            ItemStack materialStack = row1.get(column);

            for (Either<Identifier, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && material.left().get().equals(Registries.ITEM.getId(materialStack.getItem()))) {
                    hasMaterial = true;
                    break;
                } else if (material.right().isPresent() && materialStack.isIn(material.right().get())) {
                    hasMaterial = true;
                    break;
                }
            }

            if (craftingInput.getStack(column + 3).isOf(Items.STICK)){
                hasStick = true;
            }

            if (craftingInput.getStack(column + 6).isOf(Items.FEATHER)){
                hasFeather = true;
            }

            return hasMaterial && hasStick && hasFeather;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInput, DynamicRegistryManager provider) {
        List<Either<Identifier, TagKey<Item>>> materialList = new ArrayList<>();
        AtomicReference<ArrowData> arrowData = new AtomicReference<>(ArrowData.EMPTY);
        List<ItemStack> row1 = List.of(craftingInput.getStack(0), craftingInput.getStack(1), craftingInput.getStack(2));

        if (row1.stream().filter(itemStack -> !itemStack.isOf(Items.AIR)).count() != 1){
            return ItemStack.EMPTY;
        }
        int column = 0;
        for (ItemStack itemStack : row1) {
            if (!itemStack.isOf(Items.AIR)) {
                break;
            }
            column++;
        }

        ItemStack materialStack = row1.get(column);

        provider.getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().forEach(holder ->{
            materialList.add(holder.value().material());
            if (holder.value().material().left().isPresent() && holder.value().material().left().get().equals(Registries.ITEM.getId(materialStack.getItem()))) {
                arrowData.set(holder.value());
            }
            else if (holder.value().material().right().isPresent() && materialStack.isIn(holder.value().material().right().get())) {
                arrowData.set(holder.value());
            }
        });

        if (materialList.contains(arrowData.get().material())){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS, 4);
            arrowData.get().save(stack.getOrCreateNbt());
            return stack;
        }
        else{
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 1 && height >= 3;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializer.ARROW_RECIPE_SERIALIZER;
    }
}
