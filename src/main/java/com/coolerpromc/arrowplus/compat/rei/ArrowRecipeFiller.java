package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

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

        BasicDisplay.registryAccess().getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.ofItems(Items.FLINT);
            Identifier materialLocation = Identifier.of("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.ofItems(data.value().material().left().get().value());
                materialLocation = data.value().material().left().get().getKey().get().getValue();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.fromTag(data.value().material().right().get());
                materialLocation = data.value().material().right().get().id();
            }

            List<EntryIngredient> inputEntries = List.of(
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(data.value().stick().value()),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(data.value().feather().value()),
                    EntryIngredient.empty()
            );
            displays.add(new DefaultCustomDisplay(recipeHolder, inputEntries, List.of(EntryIngredients.of(output))));

            BasicDisplay.registryAccess().getWrapperOrThrow(RegistryKeys.POTION).streamEntries().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()) {
                    ItemStack arrow = output.copyWithCount(1);
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION.getRegistryEntry(), 1, ComponentChanges.builder().add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion)).build());
                    ItemStack tippedOutput = arrow.copy();
                    tippedOutput.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));

                    Identifier loc = Identifier.of(ArrowPlus.MODID, "arrowplus.arrow." + arrow.getRegistryEntry().getKey().get().getValue().getPath() + "." + potion.getKey().get().getValue().getPath());

                    List<EntryIngredient> tippedInputs = List.of(
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(lingeringPotion)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow)),
                            EntryIngredient.of(EntryStacks.of(arrow))
                    );

                    displays.add(new DefaultCustomDisplay(recipeHolder, tippedInputs, List.of(EntryIngredients.of(tippedOutput))));
                }
            });
        });
        return displays;
    }
}
