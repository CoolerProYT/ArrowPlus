package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrowRecipeFiller implements CraftingRecipeFiller<ArrowRecipe> {
    @Override
    public Class<ArrowRecipe> getRecipeClass(){
         return ArrowRecipe.class;
    }

    @Override
    public Collection<Display> apply(RecipeEntry<ArrowRecipe> recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().map(RegistryEntry.Reference::value).forEach(data -> {
            Ingredient ingredient = Ingredient.ofItems(Items.FLINT);

            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.material().left().isPresent()){
                ingredient = Ingredient.ofItems(Registries.ITEM.get(data.material().left().get()));
            }
            else if (data.material().right().isPresent()){
                ingredient = Ingredient.fromTag(data.material().right().get());
            }


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
            displays.add(new DefaultCustomDisplay(recipeHolder, inputEntries, List.of(EntryIngredients.of(output))));
        });
        return displays;
    }
}
