package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.recipe.TippedArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.fletchingrecipe.datagen.recipebuilder.FletchingRecipeBuilder;
import com.coolerpromc.fletchingrecipe.util.SizedIngredient;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(dataOutput, completableFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
        final HolderGetter<Item> items = wrapperLookup.lookupOrThrow(Registries.ITEM);
        
        return new RecipeProvider(wrapperLookup, recipeExporter) {
            @Override
            public void buildRecipes() {
                SpecialRecipeBuilder.special(ArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_recipe")));
                SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "tipped_arrow_recipe")));

                stickRecipe(Items.COPPER_INGOT, ModItems.COPPER_STICK);
                stickRecipe(Items.IRON_INGOT, ModItems.IRON_STICK);
                stickRecipe(Items.GOLD_INGOT, ModItems.GOLD_STICK);
                stickRecipe(Items.DIAMOND, ModItems.DIAMOND_STICK);
                stickRecipe(Items.EMERALD, ModItems.EMERALD_STICK);
                stickRecipe(Items.NETHERITE_INGOT, ModItems.NETHERITE_STICK);

                ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.GILDED_FEATHER, 4)
                        .pattern(" G ")
                        .pattern("GFG")
                        .pattern(" G ")
                        .define('G', Items.GLOWSTONE_DUST)
                        .define('F', Items.FEATHER)
                        .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
                        .unlockedBy(getHasName(Items.FEATHER), has(Items.FEATHER))
                        .save(output);

                registries.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().forEach(holder -> {
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
                        ingredient = Ingredient.of(items.getOrThrow(arrowData.material().right().get()));
                        hasIngredient = has(arrowData.material().right().get());
                        hasName = "has_" + arrowData.material().right().get().location().getPath();
                    }

                    FletchingRecipeBuilder.builder()
                            .top(new SizedIngredient(ingredient, 1))
                            .middle(new SizedIngredient(Ingredient.of(arrowData.stick().value()), 1))
                            .bottom(new SizedIngredient(Ingredient.of(arrowData.feather().value()), 1))
                            .output(new ItemStackTemplate(ModItems.ARROW_PLUS.builtInRegistryHolder(), arrowData.outputAmount() * 2, DataComponentPatch.builder().set(ModDataComponents.ARROW_DATA, holder).build()))
                            .unlockedBy(hasName, hasIngredient)
                            .unlockedBy(getHasName(arrowData.stick().value()), has(arrowData.stick().value()))
                            .unlockedBy(getHasName(arrowData.feather().value()), has(arrowData.feather().value()))
                            .save(withConditions(output, ResourceConditions.allModsLoaded("fletchingrecipe")), ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "fletching/" + holder.unwrapKey().get().identifier().getPath() + "_arrow")));
                });
            }

            private void stickRecipe(Item item, ModStickItem stickItem) {
                ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, stickItem, 4)
                        .pattern("S")
                        .pattern("S")
                        .define('S', Ingredient.of(item))
                        .unlockedBy(getHasName(item), has(item))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "Arrow+ Recipes";
    }
}
