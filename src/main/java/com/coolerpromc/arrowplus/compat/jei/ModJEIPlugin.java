package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.of(ArrowPlus.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getJeiHelpers().getVanillaRecipeFactory();
        String group = "arrowplus.arrow";

        List<RecipeEntry<CraftingRecipe>> recipes = new ArrayList<>();

        MinecraftClient.getInstance().world.getRegistryManager().getWrapperOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getValue().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.ofItems(Items.FLINT);
            Identifier materialLocation = Identifier.of("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.ofItems(data.value().material().left().get().value());
                materialLocation = data.value().material().left().get().getKey().get().getValue();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.fromTag(data.value().material().right().get());
                materialLocation = data.value().material().right().get().id();
            }

            Identifier id = Identifier.of(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingRecipeCategory.MISC, List.of(output))
                    .group(group)
                    .define('m', ingredient)
                    .define('s', Ingredient.ofItems(data.value().stick().value()))
                    .define('f', Ingredient.ofItems(data.value().feather().value()))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            recipes.add(new RecipeEntry<>(id, recipe));

            MinecraftClient.getInstance().world.getRegistryManager().getWrapperOrThrow(RegistryKeys.POTION).streamEntries().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()) {
                    ItemStack arrow = output.copyWithCount(1);
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION.getRegistryEntry(), 1, ComponentChanges.builder().add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion)).build());
                    ItemStack tippedOutput = arrow.copy();
                    tippedOutput.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));

                    Identifier loc = Identifier.of(ArrowPlus.MODID, "arrowplus.arrow." + arrow.getRegistryEntry().getKey().get().getValue().getPath() + "." + potion.getKey().get().getValue().getPath());
                    CraftingRecipe tippedRecipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingRecipeCategory.MISC, List.of(tippedOutput))
                            .group(group)
                            .define('a', new ComponentsIngredient(Ingredient.ofItems(arrow.getItem()), arrow.getComponentChanges()).toVanilla())
                            .define('l', new ComponentsIngredient(Ingredient.ofItems(lingeringPotion.getItem()), lingeringPotion.getComponentChanges()).toVanilla())
                            .pattern("aaa")
                            .pattern("ala")
                            .pattern("aaa")
                            .build();

                    recipes.add(new RecipeEntry<>(loc, tippedRecipe));
                }
            });
        });

        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.ARROW_PLUS, ArrowSubtypeInterpreter.INSTANCE);
    }
}
