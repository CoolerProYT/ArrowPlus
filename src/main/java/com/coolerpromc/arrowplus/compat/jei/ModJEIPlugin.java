package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.common.util.RegistryUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getJeiHelpers().getVanillaRecipeFactory();
        String group = "arrowplus.arrow";

        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).forEach(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            Identifier materialLocation = Identifier.parse("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), data.value().outputAmount());
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.of(data.value().material().left().get().value());
                materialLocation = data.value().material().left().get().unwrapKey().get().identifier();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(data.value().material().right().get()));
                materialLocation = data.value().material().right().get().location();
            }

            Identifier id = Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
            ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, id);
            SlotDisplay slotDisplay = new SlotDisplay.ItemStackSlotDisplay(output);
            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, slotDisplay)
                    .group(group)
                    .define('m', ingredient)
                    .define('s', Ingredient.of(data.value().stick().value()))
                    .define('f', Ingredient.of(data.value().feather().value()))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            recipes.add(new RecipeHolder<>(resourceKey, recipe));

            RegistryUtil.getRegistryAccess().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()) {
                    ItemStack arrow = output.copyWithCount(1);
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(potion)).build());
                    ItemStack tippedOutput = arrow.copy();
                    tippedOutput.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                    Identifier loc = Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + arrow.getItemHolder().unwrapKey().get().identifier().getPath() + "." + potion.unwrapKey().get().identifier().getPath());
                    ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, loc);
                    SlotDisplay outputDisplay = new SlotDisplay.ItemStackSlotDisplay(tippedOutput);
                    CraftingRecipe tippedRecipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, outputDisplay)
                            .group(group)
                            .define('a', DataComponentIngredient.of(true, arrow), new SlotDisplay.ItemStackSlotDisplay(arrow))
                            .define('l', DataComponentIngredient.of(true, lingeringPotion), new SlotDisplay.ItemStackSlotDisplay(lingeringPotion))
                            .pattern("aaa")
                            .pattern("ala")
                            .pattern("aaa")
                            .build();

                    recipes.add(new RecipeHolder<>(key, tippedRecipe));
                }
            });
        });

        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModItems.ARROW_PLUS.get(), ModDataComponents.ARROW_DATA.get(), DataComponents.POTION_CONTENTS);
    }
}
