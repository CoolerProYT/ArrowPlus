package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.arrow.Arrows;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.recipe.ArrowRecipe;
import com.coolerpromc.arrowplus.recipe.FeatherRecipe;
import com.coolerpromc.arrowplus.recipe.StickRecipe;
import com.coolerpromc.arrowplus.recipe.TippedArrowRecipe;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.fletchingrecipe.recipe.builder.FletchingRecipeBuilder;
import com.coolerpromc.fletchingrecipe.util.SizedIngredient;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements);
    }

    @Override
    protected void buildRecipes() {
        SpecialRecipeBuilder.special(FeatherRecipe::new).save(output, ResourceKey.create(Registries.RECIPE, Constants.id("feather_recipe")));
        SpecialRecipeBuilder.special(StickRecipe::new).save(output, ResourceKey.create(Registries.RECIPE, Constants.id("stick_recipe")));
        SpecialRecipeBuilder.special(ArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("arrow_recipe")));
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id("tipped_arrow_recipe")));

        HolderGetter<ArrowData> arrowLookup = this.output.lookup(ModRegistries.ARROW_DATA_KEY);
        HolderGetter<StickData> stickLookup = this.output.lookup(ModRegistries.STICK_DATA_KEY);
        HolderGetter<FeatherData> featherLookup = this.output.lookup(ModRegistries.FEATHER_DATA_KEY);

        Arrows.getRecipeEntries().forEach(entry -> {
            Either<Item, TagKey<Item>> material = entry.material();
            Ingredient ingredient;
            Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient;
            String hasName;
            if (material.right().isPresent()) {
                ingredient = Ingredient.of(items.getOrThrow(material.right().get()));
                hasIngredient = has(ingredient.getValues().unwrapKey().get());
                hasName = "has_" + ingredient.getValues().unwrapKey().get().location().getPath();
            } else {
                ingredient = Ingredient.of(material.left().get());
                hasIngredient = has(material.left().get());
                hasName = getHasName(material.left().get());
            }

            Ingredient stickIngredient = Ingredient.of(entry.stick());
            if (entry.stick() instanceof ModStickItem && entry.stickData().isPresent()) {
                stickIngredient = DataComponentIngredient.of(false, ModDataComponents.STICK_DATA, stickLookup.getOrThrow(entry.stickData().get()), entry.stick());
            }

            Ingredient featherIngredient = Ingredient.of(entry.feather());
            if (entry.feather() instanceof ModFeatherItem && entry.featherData().isPresent()) {
                featherIngredient = DataComponentIngredient.of(false, ModDataComponents.FEATHER_DATA, featherLookup.getOrThrow(entry.featherData().get()), entry.feather());
            }

            Holder<ArrowData> holder = arrowLookup.getOrThrow(entry.key());

            FletchingRecipeBuilder.builder()
                    .top(new SizedIngredient(ingredient, 1))
                    .middle(new SizedIngredient(stickIngredient, 1))
                    .bottom(new SizedIngredient(featherIngredient, 1))
                    .output(new ItemStackTemplate(ModItems.ARROW_PLUS.get(), entry.outputAmount() * 2, DataComponentPatch.builder().set(ModDataComponents.ARROW_DATA.get(), holder).build()))
                    .unlockedBy(hasName, hasIngredient)
                    .unlockedBy(getHasName(entry.stick()), has(entry.stick()))
                    .unlockedBy(getHasName(entry.feather()), has(entry.feather()))
                    .save(output.withConditions(NeoForgeConditions.modLoaded("fletchingrecipe")), ResourceKey.create(Registries.RECIPE, Constants.id("fletching/" + entry.key().identifier().getPath() + "_arrow")));
        });
    }
}
