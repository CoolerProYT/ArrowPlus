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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;

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

        var arrowRecipes = RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().identifier().getPath())).map(data -> {
            Ingredient ingredient = Ingredient.of(Items.FLINT);
            Identifier materialLocation = Identifier.parse("invalid");
            ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
            output.set(ModDataComponents.ARROW_DATA, data);

            if (data.value().material().left().isPresent()){
                ingredient = Ingredient.of(BuiltInRegistries.ITEM.getValue(data.value().material().left().get()));
                materialLocation = data.value().material().left().get();
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
                    .define('s', Ingredient.of(Items.STICK))
                    .define('f', Ingredient.of(Items.FEATHER))
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            return new RecipeHolder<>(resourceKey, recipe);
        }).toList();

        registration.addRecipes(RecipeTypes.CRAFTING, arrowRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModItems.ARROW_PLUS.get(), ModDataComponents.ARROW_DATA.get());
    }
}
