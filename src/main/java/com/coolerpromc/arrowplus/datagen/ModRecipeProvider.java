package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.recipe.FeatherRecipe;
import com.coolerpromc.arrowplus.recipe.StickRecipe;
import com.coolerpromc.arrowplus.recipe.TippedArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.fletchingrecipe.datagen.recipebuilder.FletchingRecipeBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider lookupProvider;

    protected ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
        this.lookupProvider = registries.join();
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        SpecialRecipeBuilder.special(FeatherRecipe::new).save(output, ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "feather_recipe"));
        SpecialRecipeBuilder.special(StickRecipe::new).save(output, ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "stick_recipe"));
        SpecialRecipeBuilder.special(ArrowRecipe::new).save(output, ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrow_recipe"));
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(output, ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "tipped_arrow_recipe"));

        lookupProvider.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().forEach(holder -> {
            ArrowData arrowData = holder.value();
            HolderSet<Item> material = arrowData.material();
            Ingredient ingredient;
            Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient = null;
            String hasName;

            if (material instanceof HolderSet.Named<Item> named) {
                ingredient = Ingredient.of(named.key());
                hasIngredient = has(named.key());
                hasName = "has_" + named.key().location().getPath();
            }
            else{
                Holder<Item> itemHolder = material.stream().findFirst().orElseThrow();
                ingredient = Ingredient.of(arrowData.material().stream().map(ItemStack::new));
                hasIngredient = has(itemHolder.value());
                hasName = getHasName(itemHolder.value());
            }

            Ingredient stickIngredient = Ingredient.of(arrowData.stick().value());
            if (arrowData.stick().value() instanceof ModStickItem && arrowData.stickData().isPresent()){
                stickIngredient = DataComponentIngredient.of(true, ModDataComponents.STICK_DATA, arrowData.stickData().get(), arrowData.stick().value());
            }

            Ingredient featherIngredient = Ingredient.of(arrowData.feather().value());
            if (arrowData.feather().value() instanceof ModFeatherItem && arrowData.featherData().isPresent()){
                featherIngredient = DataComponentIngredient.of(true, ModDataComponents.FEATHER_DATA, arrowData.featherData().get(), arrowData.feather().value());
            }

            FletchingRecipeBuilder.builder()
                    .top(new SizedIngredient(ingredient, 1))
                    .middle(new SizedIngredient(stickIngredient, 1))
                    .bottom(new SizedIngredient(featherIngredient, 1))
                    .output(new ItemStack(ModItems.ARROW_PLUS, arrowData.outputAmount() * 2, DataComponentPatch.builder().set(ModDataComponents.ARROW_DATA.get(), holder).build()))
                    .unlockedBy(hasName, hasIngredient)
                    .unlockedBy(getHasName(arrowData.stick().value()), has(arrowData.stick().value()))
                    .unlockedBy(getHasName(arrowData.feather().value()), has(arrowData.feather().value()))
                    .save(output.withConditions(new ModLoadedCondition("fletchingrecipe")), ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "fletching/" + holder.getKey().location().getPath() + "_arrow"));
        });
    }
}
