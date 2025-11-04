package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.coolerpromc.arrowplus.util.ArrowData;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.common.util.RegistryUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;
import java.util.stream.Collectors;

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

        List<CraftingRecipe> arrowRecipes = RegistryUtil.getRegistryAccess().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements()
                .filter(reference -> !ArrowPlusConfig.CONFIG.getRemoval().contains(reference.key().location().getPath()))
                .map(lookup -> {
                    System.out.println(lookup.key());
                    ArrowData data = lookup.value();
                    Ingredient ingredient = Ingredient.of(Items.FLINT);
                    ResourceLocation materialLocation = ResourceLocation.parse("invalid");
                    ItemStack output = new ItemStack(ModItems.ARROW_PLUS.get(), 4);
                    data.save(output.getOrCreateTag(), RegistryUtil.getRegistryAccess());

                    if (data.material().left().isPresent()) {
                        ingredient = Ingredient.of(BuiltInRegistries.ITEM.get(data.material().left().get()));
                        materialLocation = data.material().left().get();
                    } else if (data.material().right().isPresent()) {
                        ingredient = Ingredient.of(data.material().right().get());
                        materialLocation = data.material().right().get().location();
                    }

                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "arrowplus.arrow." + materialLocation.getPath());
                    NonNullList<Ingredient> inputs = NonNullList.of(
                            Ingredient.EMPTY,
                            ingredient,
                            Ingredient.EMPTY,
                            Ingredient.EMPTY,
                            Ingredient.of(Items.STICK),
                            Ingredient.EMPTY,
                            Ingredient.EMPTY,
                            Ingredient.of(Items.FEATHER),
                            Ingredient.EMPTY
                    );
                    return (new ShapedRecipe(id, group, CraftingBookCategory.MISC, 3, 3, inputs, output));
                }).collect(Collectors.toUnmodifiableList());

        registration.addRecipes(RecipeTypes.CRAFTING, arrowRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.ARROW_PLUS.get(), ArrowSubtypeInterpreter.INSTANCE);
    }
}
