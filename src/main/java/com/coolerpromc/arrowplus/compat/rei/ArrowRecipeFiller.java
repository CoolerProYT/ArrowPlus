package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrowRecipeFiller implements CraftingRecipeFiller<ArrowRecipe> {
    public Class<ArrowRecipe> getRecipeClass(){
         return ArrowRecipe.class;
    }

    @Override
    public Collection<Display> apply(RecipeHolder<ArrowRecipe> recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.get(data.value().material().left().get()));
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.of(data.value().material().right().get());
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
