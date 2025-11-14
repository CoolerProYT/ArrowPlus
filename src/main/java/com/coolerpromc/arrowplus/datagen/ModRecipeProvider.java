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
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.component.ComponentChanges;
import net.minecraft.data.server.recipe.ComplexRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    private final RegistryWrapper.WrapperLookup registries;

    public ModRecipeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(dataOutput, completableFuture);
        this.registries = completableFuture.join();
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ComplexRecipeJsonBuilder.create(ArrowRecipe::new).offerTo(exporter, Identifier.of(ArrowPlus.MODID, "arrow_recipe"));
        ComplexRecipeJsonBuilder.create(TippedArrowRecipe::new).offerTo(exporter, Identifier.of(ArrowPlus.MODID, "tipped_arrow_recipe"));

        stickRecipe(Items.COPPER_INGOT, ModItems.COPPER_STICK, exporter);
        stickRecipe(Items.IRON_INGOT, ModItems.IRON_STICK, exporter);
        stickRecipe(Items.GOLD_INGOT, ModItems.GOLD_STICK, exporter);
        stickRecipe(Items.DIAMOND, ModItems.DIAMOND_STICK, exporter);
        stickRecipe(Items.EMERALD, ModItems.EMERALD_STICK, exporter);
        stickRecipe(Items.NETHERITE_INGOT, ModItems.NETHERITE_STICK, exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GILDED_FEATHER, 4)
                .pattern(" G ")
                .pattern("GFG")
                .pattern(" G ")
                .input('G', Items.GLOWSTONE_DUST)
                .input('F', Items.FEATHER)
                .criterion(hasItem(Items.GLOWSTONE_DUST), conditionsFromItem(Items.GLOWSTONE_DUST))
                .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                .offerTo(exporter);

        registries.getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().forEach(holder -> {
            ArrowData arrowData = holder.value();
            Ingredient ingredient = null;
            AdvancementCriterion<InventoryChangedCriterion.Conditions> hasIngredient = null;
            String hasName = "";
            if (arrowData.material().left().isPresent()){
                ingredient = Ingredient.ofItems(arrowData.material().left().get().value());
                hasIngredient = conditionsFromItem(arrowData.material().left().get().value());
                hasName = hasItem(arrowData.material().left().get().value());
            }
            else if (arrowData.material().right().isPresent()){
                ingredient = Ingredient.fromTag(arrowData.material().right().get());
                hasIngredient = conditionsFromTag(arrowData.material().right().get());
                hasName = "has_" + arrowData.material().right().get().id().getPath();
            }

            FletchingRecipeBuilder.builder()
                    .top(new SizedIngredient(ingredient, 1))
                    .middle(new SizedIngredient(Ingredient.ofItems(arrowData.stick().value()), 1))
                    .bottom(new SizedIngredient(Ingredient.ofItems(arrowData.feather().value()), 1))
                    .output(new ItemStack(ModItems.ARROW_PLUS.getRegistryEntry(), arrowData.outputAmount() * 2, ComponentChanges.builder().add(ModDataComponents.ARROW_DATA, holder).build()))
                    .criterion(hasName, hasIngredient)
                    .criterion(hasItem(arrowData.stick().value()), conditionsFromItem(arrowData.stick().value()))
                    .criterion(hasItem(arrowData.feather().value()), conditionsFromItem(arrowData.feather().value()))
                    .offerTo(withConditions(exporter, ResourceConditions.allModsLoaded("fletchingrecipe")), Identifier.of(ArrowPlus.MODID, "fletching/" + holder.getKey().get().getValue().getPath() + "_arrow"));
        });
    }

    private void stickRecipe(Item item, ModStickItem stickItem, RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, stickItem, 4)
                .pattern("S")
                .pattern("S")
                .input('S', Ingredient.ofItems(item))
                .criterion(hasItem(item), conditionsFromItem(item))
                .offerTo(exporter);
    }

    @Override
    public String getName() {
        return "Arrow+ Recipes";
    }
}
