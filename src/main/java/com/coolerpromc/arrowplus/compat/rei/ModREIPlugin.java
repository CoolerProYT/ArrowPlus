package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datagen.datapack.ArrowRecipe;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

@REIPluginClient
public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).forEach(reference -> {
            ArrowData data = reference.value();
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            data.save(output.getOrCreateTag(), BasicDisplay.registryAccess());

            if (data.material().left().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.get(data.material().left().get()));
            }
            else if (data.material().right().isPresent()){
                ingredient = Ingredient.of(data.material().right().get());
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
