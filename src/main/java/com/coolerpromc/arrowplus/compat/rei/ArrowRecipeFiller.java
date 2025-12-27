package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ArrowRecipeFiller implements Function<ArrowRecipe, Collection<Display>> {
     public void registerDisplays(DisplayRegistry registry) {
        registry.beginFiller(getRecipeClass())
                .fillMultiple(this);
    }

    Class<ArrowRecipe> getRecipeClass(){
         return ArrowRecipe.class;
    }

    @Override
    public Collection<Display> apply(ArrowRecipe recipeHolder) {
        List<Display> displays = new ArrayList<>();

        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.getKey().location().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            ResourceLocation materialLocation = ResourceLocation.parse("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.of(data.value().material().left().get().value());
                materialLocation = data.value().material().left().get().getKey().location();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(data.value().material().right().get()));
                materialLocation = data.value().material().right().get().location();
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());

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
            displays.add(new DefaultCustomDisplay(inputEntries, List.of(EntryIngredients.of(output)), Optional.of(id)));

            BasicDisplay.registryAccess().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()) {
                    ItemStack arrow = new ItemStack(ModItems.ARROW_PLUS.getDelegate(), 1, DataComponentPatch.builder().set(ModDataComponents.ARROW_DATA.get(), data).build());
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(potion)).build());
                    ItemStack tippedOutput = arrow.copy();
                    tippedOutput.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                    ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + arrow.getItemHolder().getKey().location().getPath() + "." + potion.getKey().location().getPath());

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

                    displays.add(new DefaultCustomDisplay(tippedInputs, List.of(EntryIngredients.of(tippedOutput)), Optional.of(loc)));
                }
            });
        });
        return displays;
    }
}
