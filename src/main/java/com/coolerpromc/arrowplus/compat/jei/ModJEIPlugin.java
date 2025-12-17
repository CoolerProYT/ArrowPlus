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
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.of(ArrowPlus.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getJeiHelpers().getVanillaRecipeFactory();
        String group = "arrowplus.arrow";

        var arrowRecipes = MinecraftClient.getInstance().world.getRegistryManager().getOrThrow(ModRegistries.ARROW_DATA_KEY).streamEntries().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.registryKey().getRegistry().getPath())).map(data -> {
            Ingredient ingredient = Ingredient.ofItem(Items.FLINT);
            Identifier materialLocation = Identifier.of("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS, 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.ofItem(Registries.ITEM.get(data.value().material().left().get()));
                materialLocation = data.value().material().left().get();
            }
            else if (data.value().material().right().isPresent()){
                ingredient = Ingredient.ofTag(Registries.ITEM.getOrThrow(data.value().material().right().get()));
                materialLocation = data.value().material().right().get().id();
            }

            Identifier id = Identifier.of(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
            RegistryKey<Recipe<?>> resourceKey = RegistryKey.of(RegistryKeys.RECIPE, id);
            SlotDisplay slotDisplay = new SlotDisplay.StackSlotDisplay(output);
            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingRecipeCategory.MISC, slotDisplay)
                    .group(group)
                    .define('m', ingredient)
                    .define('s', Ingredient.ofItem(Items.STICK))
                    .define('f', Ingredient.ofItem(Items.FEATHER))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            return new RecipeEntry<>(resourceKey, recipe);
        }).toList();

        registration.addRecipes(RecipeTypes.CRAFTING, arrowRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModItems.ARROW_PLUS, ModDataComponents.ARROW_DATA);
    }
}
