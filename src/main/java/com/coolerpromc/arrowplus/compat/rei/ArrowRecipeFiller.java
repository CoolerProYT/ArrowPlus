package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ArrowRecipeFiller implements Function<RecipeEntry<ArrowRecipe>, Collection<Display>> {
     public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(getRecipeClass())
                .filterType(RecipeType.CRAFTING)
                .fillMultiple(this);
    }

    Class<ArrowRecipe> getRecipeClass(){
         return ArrowRecipe.class;
    }

    @Override
    public Collection<Display> apply(RecipeEntry<ArrowRecipe> recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.ofItem(Items.FLINT);
            Identifier materialLocation = Identifier.of("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.ofItem(Registries.ITEM.get(data.value().material().left().get()));
                materialLocation = data.value().material().left().get();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.ofTag(Registries.ITEM.getOrThrow(data.value().material().right().get()));
                materialLocation = data.value().material().right().get().id();
            }

            Identifier id = Identifier.of(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());

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
