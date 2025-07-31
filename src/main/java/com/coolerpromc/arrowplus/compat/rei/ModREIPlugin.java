package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;

public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BasicDisplay.registryAccess().getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().map(RegistryEntry.Reference::value).forEach(data -> {
            Ingredient ingredient = Ingredient.ofItems(Items.FLINT);

            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, 4);
            data.save(output.getOrCreateNbt());

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
            registry.add(new DefaultCraftingDisplay<ArrowRecipe>(inputEntries, List.of(EntryIngredients.of(output)), Optional.empty()) {
                @Override
                public int getWidth() {
                    return 3;
                }

                @Override
                public int getHeight() {
                    return 3;
                }
            });
        });
    }
}
