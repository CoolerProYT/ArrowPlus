package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.arrow.Arrows;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.recipe.FeatherRecipe;
import com.coolerpromc.arrowplus.recipe.StickRecipe;
import com.coolerpromc.arrowplus.recipe.TippedArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.fletchingrecipe.recipe.builder.FletchingRecipeBuilder;
import com.coolerpromc.fletchingrecipe.util.SizedIngredient;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AllModsLoadedResourceCondition;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    protected ModRecipeProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(dataOutput, completableFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                SpecialRecipeBuilder.special(FeatherRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("feather_recipe")));
                SpecialRecipeBuilder.special(StickRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("stick_recipe")));
                SpecialRecipeBuilder.special(ArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("arrow_recipe")));
                SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("tipped_arrow_recipe")));

                Holder<StickData> stick = registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY).getOrThrow(Sticks.STICK);
                Holder<FeatherData> feather = registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).getOrThrow(Feathers.FEATHER);

                registries.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(holder -> !holder.is(Arrows.FLINT)).forEach(holder -> {
                    ArrowData arrowData = holder.value();
                    Ingredient ingredient = arrowData.ingredient();
                    Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient;
                    String hasName;
                    if (ingredient.values.unwrapKey().isPresent()){
                        hasIngredient = has(ingredient.values.unwrapKey().get());
                        hasName = "has_" + ingredient.values.unwrapKey().get().location().getPath();
                    }
                    else{
                        hasIngredient = has(ingredient.values.get(0).value());
                        hasName = getHasName(ingredient.values.get(0).value());
                    }

                    FletchingRecipeBuilder.builder()
                            .top(new SizedIngredient(ingredient, 1))
                            .middle(new SizedIngredient(Ingredient.of(Items.STICK), 1))
                            .bottom(new SizedIngredient(Ingredient.of(Items.FEATHER), 1))
                            .output(new ItemStackTemplate(ModItems.ARROW_PLUS.get().builtInRegistryHolder(), arrowData.outputAmount() * 2, DataComponentPatch.builder()
                                    .set(ModDataComponents.ARROW_DATA.get(), holder)
                                    .set(ModDataComponents.STICK_DATA.get(), stick)
                                    .set(ModDataComponents.FEATHER_DATA.get(), feather)
                                    .build()))
                            .unlockedBy(hasName, hasIngredient)
                            .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                            .unlockedBy(getHasName(Items.FEATHER), has(Items.FEATHER))
                            .save(withConditions(this.output, new AllModsLoadedResourceCondition(List.of("fletchingrecipe"))), ResourceKey.create(Registries.RECIPE, Constants.id("fletching/" + holder.key().identifier().getPath() + "_arrow")));
                });
            }
        };
    }

    @Override
    public String getName() {
        return "Arrow+ Recipes";
    }
}
