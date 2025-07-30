package com.coolerpromc.arrowplus.datagen.datapack;

import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArrowRecipe extends CustomRecipe {
    public ArrowRecipe(ResourceLocation resourceLocation, CraftingBookCategory category) {
        super(resourceLocation, category);
    }

    @Override
    public boolean matches(CraftingContainer craftingInput, Level level) {
        if (craftingInput.getWidth() == 3 && craftingInput.getHeight() == 3){
            List<Either<ResourceLocation, TagKey<Item>>> materialList = new ArrayList<>();
            level.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().map(Holder.Reference::value).forEach(arrowData -> {
                materialList.add(arrowData.material());
            });

            boolean hasMaterial = false;
            boolean hasStick = false;
            boolean hasFeather = false;

            List<ItemStack> row1 = List.of(craftingInput.getItem(0), craftingInput.getItem(1), craftingInput.getItem(2));

            if (row1.stream().filter(itemStack -> !itemStack.is(Items.AIR)).count() != 1){
                return false;
            }
            int column = 0;
            for (ItemStack itemStack : row1) {
                if (!itemStack.is(Items.AIR)) {
                    break;
                }
                column++;
            }

            ItemStack materialStack = row1.get(column);

            for (Either<ResourceLocation, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && material.left().get().equals(BuiltInRegistries.ITEM.getKey(materialStack.getItem()))) {
                    hasMaterial = true;
                    break;
                } else if (material.right().isPresent() && materialStack.is(material.right().get())) {
                    hasMaterial = true;
                    break;
                }
            }

            if (craftingInput.getItem(column + 3).is(Items.STICK)){
                hasStick = true;
            }

            if (craftingInput.getItem(column + 6).is(Items.FEATHER)){
                hasFeather = true;
            }

            return hasMaterial && hasStick && hasFeather;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingInput, RegistryAccess provider) {
        List<Either<ResourceLocation, TagKey<Item>>> materialList = new ArrayList<>();
        AtomicReference<ArrowData> arrowData = new AtomicReference<>(ArrowData.EMPTY);
        List<ItemStack> row1 = List.of(craftingInput.getItem(0), craftingInput.getItem(1), craftingInput.getItem(2));

        if (row1.stream().filter(itemStack -> !itemStack.is(Items.AIR)).count() != 1){
            return ItemStack.EMPTY;
        }
        int column = 0;
        for (ItemStack itemStack : row1) {
            if (!itemStack.is(Items.AIR)) {
                break;
            }
            column++;
        }

        ItemStack materialStack = row1.get(column);

        provider.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().forEach(holder ->{
            materialList.add(holder.value().material());
            if (holder.value().material().left().isPresent() && holder.value().material().left().get().equals(BuiltInRegistries.ITEM.getKey(materialStack.getItem()))) {
                arrowData.set(holder.value());
            }
            else if (holder.value().material().right().isPresent() && materialStack.is(holder.value().material().right().get())) {
                arrowData.set(holder.value());
            }
        });

        if (materialList.contains(arrowData.get().material())){
            ItemStack stack = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            arrowData.get().save(stack.getOrCreateTag());
            return stack;
        }
        else{
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 1 && height >= 3;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializer.ARROW_RECIPE_SERIALIZER.get();
    }
}
