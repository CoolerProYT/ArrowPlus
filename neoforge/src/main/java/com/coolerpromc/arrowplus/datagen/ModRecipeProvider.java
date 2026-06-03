package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.recipe.TippedArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.fletchingrecipe.datagen.recipebuilder.FletchingRecipeBuilder;
import com.coolerpromc.fletchingrecipe.util.SizedIngredient;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider lookupProvider;

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.lookupProvider = registries;
    }

    @Override
    protected void buildRecipes() {
        SpecialRecipeBuilder.special(ArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("arrow_recipe")));
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("tipped_arrow_recipe")));

        stickRecipe(Items.COPPER_INGOT, ModItems.COPPER_STICK.get());
        stickRecipe(Items.IRON_INGOT, ModItems.IRON_STICK.get());
        stickRecipe(Items.GOLD_INGOT, ModItems.GOLD_STICK.get());
        stickRecipe(Items.DIAMOND, ModItems.DIAMOND_STICK.get());
        stickRecipe(Items.EMERALD, ModItems.EMERALD_STICK.get());
        stickRecipe(Items.NETHERITE_INGOT, ModItems.NETHERITE_STICK.get());

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.GILDED_FEATHER.get(), 4)
                .pattern(" G ")
                .pattern("GFG")
                .pattern(" G ")
                .define('G', Items.GLOWSTONE_DUST)
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
                .unlockedBy(getHasName(Items.FEATHER), has(Items.FEATHER))
                .save(output);

        lookupProvider.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().forEach(holder -> {
            ArrowData arrowData = holder.value();
            Ingredient ingredient = null;
            Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient = null;
            String hasName = "";
            if (arrowData.material().left().isPresent()){
                ingredient = Ingredient.of(arrowData.material().left().get().value());
                hasIngredient = has(arrowData.material().left().get().value());
                hasName = getHasName(arrowData.material().left().get().value());
            }
            else if (arrowData.material().right().isPresent()){
                ingredient = Ingredient.of(lookupProvider.lookupOrThrow(Registries.ITEM).getOrThrow(arrowData.material().right().get()));
                hasIngredient = has(arrowData.material().right().get());
                hasName = "has_" + arrowData.material().right().get().location().getPath();
            }

            FletchingRecipeBuilder.builder()
                    .top(new SizedIngredient(ingredient, 1))
                    .middle(new SizedIngredient(Ingredient.of(arrowData.stick().value()), 1))
                    .bottom(new SizedIngredient(Ingredient.of(arrowData.feather().value()), 1))
                    .output(new ItemStackTemplate(ModItems.ARROW_PLUS.get(), arrowData.outputAmount() * 2, DataComponentPatch.builder().set(ModDataComponents.ARROW_DATA.get(), holder).build()))
                    .unlockedBy(hasName, hasIngredient)
                    .unlockedBy(getHasName(arrowData.stick().value()), has(arrowData.stick().value()))
                    .unlockedBy(getHasName(arrowData.feather().value()), has(arrowData.feather().value()))
                    .save(output.withConditions(NeoForgeConditions.modLoaded("fletchingrecipe")), ResourceKey.create(Registries.RECIPE, Constants.id("fletching/" + holder.getKey().identifier().getPath() + "_arrow")));
        });
    }

    private void stickRecipe(Item item, ModStickItem stickItem){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, stickItem, 4)
                .pattern("S")
                .pattern("S")
                .define('S', Ingredient.of(item))
                .unlockedBy(getHasName(item), has(item))
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Arrow+ Recipes";
        }
    }
}
