package com.coolerpromc.arrowplus.compat.rei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
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

        BasicDisplay.registryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.getKey().location().getPath())).forEach(data -> {
            Ingredient ingredient;
            HolderSet<Item> material = data.value().material();
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (material instanceof HolderSet.Named<Item> named) {
                ingredient = Ingredient.of(named.key());
            } else {
                ingredient = Ingredient.of(material.stream().map(ItemStack::new));
            }

            ItemStack feather = data.value().feather().value().getDefaultInstance();
            if (feather.getItem() instanceof ModFeatherItem && data.value().featherData().isPresent()){
                feather.set(ModDataComponents.FEATHER_DATA, data.value().featherData().get());
            }

            ItemStack stick = data.value().stick().value().getDefaultInstance();
            if (stick.getItem() instanceof ModStickItem && data.value().stickData().isPresent()){
                stick.set(ModDataComponents.STICK_DATA, data.value().stickData().get());
            }

            List<EntryIngredient> inputEntries = List.of(
                    EntryIngredient.empty(),
                    EntryIngredients.ofIngredient(ingredient),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(stick),
                    EntryIngredient.empty(),
                    EntryIngredient.empty(),
                    EntryIngredients.of(feather),
                    EntryIngredient.empty()
            );
            displays.add(new DefaultCustomDisplay(recipeHolder, inputEntries, List.of(EntryIngredients.of(output))));

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

                    displays.add(new DefaultCustomDisplay(loc, null, tippedInputs, List.of(EntryIngredients.of(tippedOutput))));
                }
            });
        });
        return displays;
    }
}
