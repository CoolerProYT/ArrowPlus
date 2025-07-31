package com.coolerpromc.arrowplus.compat;

import com.coolerpromc.arrowplus.ArrowPlus;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

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

        var arrowRecipes = RegistryUtil.getRegistry(ModRegistries.ARROW_DATA_KEY).stream().map(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            ResourceLocation materialLocation = ResourceLocation.parse("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            output.set(ModDataComponents.ARROW_DATA.get(), data);

            if (data.material().left().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.get(data.material().left().get()));
                materialLocation = data.material().left().get();
            }
            else if (data.material().right().isPresent()){
                ingredient = Ingredient.of(data.material().right().get());
                materialLocation = data.material().right().get().location();
            }

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(output))
                    .group(group)
                    .define('m', ingredient)
                    .define('s', Ingredient.of(Items.STICK))
                    .define('f', Ingredient.of(Items.FEATHER))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            return new RecipeHolder<>(id, recipe);
        }).toList();

        registration.addRecipes(RecipeTypes.CRAFTING, arrowRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.ARROW_PLUS.get(), ArrowSubtypeInterpreter.INSTANCE);
    }
}