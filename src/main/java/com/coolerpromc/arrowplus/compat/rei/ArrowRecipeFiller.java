package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ArrowRecipeFiller implements Function<RecipeHolder<ArrowRecipe>, Collection<Display>> {
     public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(getRecipeClass())
                .filterType(RecipeType.CRAFTING)
                .fillMultiple(this);
    }

    Class<ArrowRecipe> getRecipeClass(){
         return ArrowRecipe.class;
    }

    @Override
    public Collection<Display> apply(RecipeHolder<ArrowRecipe> recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).stream().forEach(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            ResourceLocation materialLocation = ResourceLocation.parse("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.material().left().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.getValue(data.material().left().get()));
                materialLocation = data.material().left().get();
            }
            else if (data.material().right().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(data.material().right().get()));
                materialLocation = data.material().right().get().location();
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());

            List<EntryIngredient> inputEntries = List.of(
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(Items.STICK),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(Items.FEATHER),
                    EntryIngredient.empty()
            );
            displays.add(new DefaultCustomDisplay(inputEntries, List.of(EntryIngredients.of(output)), Optional.of(id)));
        });
        return displays;
    }
}
