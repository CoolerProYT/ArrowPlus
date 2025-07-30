package com.coolerpromc.arrowplus.datagen.datapack;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.ModRecipeSerializer;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ArrowRecipe extends CustomRecipe {

    public ArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.width() == 1 && craftingInput.height() == 3 && craftingInput.ingredientCount() == 3){
            List<Either<ResourceLocation, TagKey<Item>>> materialList = new ArrayList<>();
            level.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().map(Holder.Reference::value).forEach(arrowData -> {
                materialList.add(arrowData.material());
            });

            boolean hasMaterial = false;
            boolean hasStick = false;
            boolean hasFeather = false;

            ItemStack materialStack = craftingInput.getItem(0);

            for (Either<ResourceLocation, TagKey<Item>> material : materialList){
                if (material.left().isPresent() && material.left().get().equals(BuiltInRegistries.ITEM.getKey(materialStack.getItem()))) {
                    hasMaterial = true;
                    break;
                } else if (material.right().isPresent() && materialStack.is(material.right().get())) {
                    hasMaterial = true;
                    break;
                }
            }

            if (craftingInput.getItem(1).is(Items.STICK)){
                hasStick = true;
            }

            if (craftingInput.getItem(2).is(Items.FEATHER)){
                hasFeather = true;
            }

            return hasMaterial && hasStick && hasFeather;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        List<Either<ResourceLocation, TagKey<Item>>> materialList = new ArrayList<>();
        AtomicReference<ArrowData> arrowData = new AtomicReference<>(ArrowData.EMPTY);
        ItemStack materialStack = craftingInput.getItem(0);

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
            stack.set(ModDataComponents.ARROW_DATA, arrowData.get());
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
