package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.FeatherRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatherRecipeFiller implements CraftingRecipeFiller<FeatherRecipe> {
    public Class<FeatherRecipe> getRecipeClass(){
         return FeatherRecipe.class;
    }

    @Override
    public Collection<Display> apply(RecipeHolder<FeatherRecipe> recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().forEach(data -> {
            Ingredient ingredient;
            HolderSet<Item> material = data.value().material();
            ItemStack output = new ItemStack(ModItems.CUSTOM_FEATHER.get(), data.value().outputAmount());
            output.set(ModDataComponents.FEATHER_DATA, data);

            if (material instanceof HolderSet.Named<Item> named) {
                ingredient = Ingredient.of(named.key());
            } else {
                ingredient = Ingredient.of(material.stream().map(ItemStack::new));
            }

            List<EntryIngredient> inputEntries = List.of(
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredients.of(Items.FEATHER),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty()
            );
            displays.add(new DefaultCustomDisplay(recipeHolder, inputEntries, List.of(EntryIngredients.of(output))));
        });
        return displays;
    }
}
