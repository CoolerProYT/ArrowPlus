package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.RegistryUtil;
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
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getJeiHelpers().getVanillaRecipeFactory();
        String group = "arrowplus.arrow";

        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().forEach(data -> {
            Ingredient ingredient;
            HolderSet<Item> material = data.value().material();

            ResourceLocation materialLocation;
            ItemStack output = new ItemStack(ModItems.CUSTOM_FEATHER.get(), data.value().outputAmount());
            output.set(ModDataComponents.FEATHER_DATA, data);

            if (material instanceof HolderSet.Named<Item> named) {
                materialLocation = named.key().location();
                ingredient = Ingredient.of(named.key());
            } else {
                Holder<Item> holder = material.stream().findFirst().orElseThrow();
                materialLocation = holder.unwrapKey().orElseThrow().location();
                ingredient = Ingredient.of(material.stream().map(ItemStack::new));
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.feather." + materialLocation.getPath());

            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(output))
                    .group(group)
                    .define('m', ingredient)
                    .define('f', Ingredient.of(Items.FEATHER))
                    .pattern(" m ")
                    .pattern("mfm")
                    .pattern(" m ")
                    .build();
            recipes.add(new RecipeHolder<>(id, recipe));
        });

        RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().forEach(data -> {
            Ingredient ingredient;
            HolderSet<Item> material = data.value().material();

            ResourceLocation materialLocation;
            ItemStack output = new ItemStack(ModItems.CUSTOM_STICK.get(), data.value().outputAmount());
            output.set(ModDataComponents.STICK_DATA, data);

            if (material instanceof HolderSet.Named<Item> named) {
                materialLocation = named.key().location();
                ingredient = Ingredient.of(named.key());
            } else {
                Holder<Item> holder = material.stream().findFirst().orElseThrow();
                materialLocation = holder.unwrapKey().orElseThrow().location();
                ingredient = Ingredient.of(material.stream().map(ItemStack::new));
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.stick." + materialLocation.getPath());

            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(output))
                    .group(group)
                    .define('m', ingredient)
                    .pattern("m")
                    .pattern("m")
                    .build();
            recipes.add(new RecipeHolder<>(id, recipe));
        });

        RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).forEach(data -> {
            Ingredient ingredient;
            HolderSet<Item> material = data.value().material();

            ResourceLocation materialLocation;
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (material instanceof HolderSet.Named<Item> named) {
                materialLocation = named.key().location();
                ingredient = Ingredient.of(named.key());
            } else {
                Holder<Item> holder = material.stream().findFirst().orElseThrow();
                materialLocation = holder.unwrapKey().orElseThrow().location();
                ingredient = Ingredient.of(material.stream().map(ItemStack::new));
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
            ItemStack feather = data.value().feather().value().getDefaultInstance();
            if (feather.getItem() instanceof ModFeatherItem && data.value().featherData().isPresent()){
                feather.set(ModDataComponents.FEATHER_DATA, data.value().featherData().get());
            }

            ItemStack stick = data.value().stick().value().getDefaultInstance();
            if (stick.getItem() instanceof ModStickItem && data.value().stickData().isPresent()){
                stick.set(ModDataComponents.STICK_DATA, data.value().stickData().get());
            }

            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(output))
                    .group(group)
                    .define('m', ingredient)
                    .define('s', Ingredient.of(stick))
                    .define('f', Ingredient.of(feather))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            recipes.add(new RecipeHolder<>(id, recipe));

            RegistryUtil.getRegistryAccess().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()) {
                    ItemStack arrow = output.copyWithCount(1);
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(potion)).build());
                    ItemStack tippedOutput = arrow.copy();
                    tippedOutput.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                    ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + arrow.getItemHolder().unwrapKey().get().location().getPath() + "." + potion.unwrapKey().get().location().getPath());
                    CraftingRecipe tippedRecipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(tippedOutput))
                            .group(group)
                            .define('a', DataComponentIngredient.of(true, arrow))
                            .define('l', DataComponentIngredient.of(true, lingeringPotion))
                            .pattern("aaa")
                            .pattern("ala")
                            .pattern("aaa")
                            .build();

                    recipes.add(new RecipeHolder<>(loc, tippedRecipe));
                }
            });
        });

        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.ARROW_PLUS.get(), ArrowSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModItems.CUSTOM_FEATHER.get(), FeatherSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModItems.CUSTOM_STICK.get(), StickSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        if (ArrowPlusConfig.CONFIG.hideTippedArrow()){
            IIngredientManager ingredientManager = registration.getJeiHelpers().getIngredientManager();

            RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath())).forEach(data -> {
                ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get());
                output.set(ModDataComponents.ARROW_DATA, data);
                List<ItemStack> toRemove = new ArrayList<>();

                RegistryUtil.getRegistryAccess().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                    if (!potion.value().getEffects().isEmpty()) {
                        ItemStack arrow = output.copyWithCount(1);
                        ItemStack tippedOutput = arrow.copy();
                        tippedOutput.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                        toRemove.add(tippedOutput);
                    }
                });
                ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, toRemove);
                ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Collections.singletonList(output));
            });
        }
    }
}
