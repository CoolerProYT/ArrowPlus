package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.item.ModItems;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Constants.id("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getJeiHelpers().getVanillaRecipeFactory();
        String group = "arrowplus.arrow";

        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

        registryAccess.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().filter(data -> !data.value().standalone()).forEach(data -> {
            Ingredient ingredient = data.value().ingredient();

            Identifier materialLocation = data.key().identifier();
            ItemStackTemplate output = new ItemStackTemplate(ModItems.CUSTOM_FEATHER.get().builtInRegistryHolder(), data.value().outputAmount(), DataComponentPatch.builder().set(ModDataComponents.FEATHER_DATA.get(), data).build());

            ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, Constants.id("arrowplus.feather." + materialLocation.getPath()));

            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, new SlotDisplay.ItemStackSlotDisplay(output))
                    .group(group)
                    .define('m', ingredient)
                    .define('f', Ingredient.of(Items.FEATHER))
                    .pattern(" m ")
                    .pattern("mfm")
                    .pattern(" m ")
                    .build();
            recipes.add(new RecipeHolder<>(id, recipe));
        });

        registryAccess.lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().filter(data -> !data.value().standalone()).forEach(data -> {
            Ingredient ingredient = data.value().ingredient();

            Identifier materialLocation = data.key().identifier();
            ItemStackTemplate output = new ItemStackTemplate(ModItems.CUSTOM_STICK.get().builtInRegistryHolder(), data.value().outputAmount(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), data).build());

            ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, Constants.id("arrowplus.stick." + materialLocation.getPath()));

            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, new SlotDisplay.ItemStackSlotDisplay(output))
                    .group(group)
                    .define('m', ingredient)
                    .pattern("m")
                    .pattern("m")
                    .build();
            recipes.add(new RecipeHolder<>(id, recipe));
        });

        List<ItemStack> sticks = registryAccess.lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().flatMap(data -> partStacks(data, data.value().standalone(), data.value().material(), ModItems.CUSTOM_STICK.get(), ModDataComponents.STICK_DATA.get()).stream()).toList();
        List<ItemStack> feathers = registryAccess.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().flatMap(data -> partStacks(data, data.value().standalone(), data.value().material(), ModItems.CUSTOM_FEATHER.get(), ModDataComponents.FEATHER_DATA.get()).stream()).toList();
        Ingredient stickIngredient = Ingredient.of(sticks.stream().map(ItemStack::getItem).distinct().toArray(Item[]::new));
        Ingredient featherIngredient = Ingredient.of(feathers.stream().map(ItemStack::getItem).distinct().toArray(Item[]::new));
        SlotDisplay stickDisplay = new SlotDisplay.Composite(sticks.stream().<SlotDisplay>map(stack -> new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(stack))).toList());
        SlotDisplay featherDisplay = new SlotDisplay.Composite(feathers.stream().<SlotDisplay>map(stack -> new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(stack))).toList());

        registryAccess.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.getRemoval().contains(reference.key().identifier().getPath())).forEach(data -> {
            Ingredient ingredient = data.value().ingredient();
            Identifier materialLocation = data.key().identifier();
            ItemStackTemplate output = ItemStackTemplate.fromNonEmptyStack(ArrowParts.createDefault(data, registryAccess, data.value().outputAmount()));

            Identifier id = JeiRecipeIds.arrow(materialLocation);
            ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, id);

            SlotDisplay slotDisplay = new SlotDisplay.ItemStackSlotDisplay(output);
            CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, slotDisplay)
                    .group(group)
                    .define('m', ingredient)
                    .define('s', stickIngredient, stickDisplay)
                    .define('f', featherIngredient, featherDisplay)
                    .pattern(" m ")
                    .pattern(" s ")
                    .pattern(" f ")
                    .build();
            recipes.add(new RecipeHolder<>(resourceKey, recipe));

            registryAccess.lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                if (!potion.value().getEffects().isEmpty()){
                    ItemStackTemplate arrow = copyFrom(output);
                    ItemStackTemplate lingeringPotion = new ItemStackTemplate(Items.LINGERING_POTION.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(potion)).build());
                    ItemStackTemplate tippedOutput = copyFromWithComponents(arrow, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(potion)).build());

                    Identifier loc = JeiRecipeIds.tipped(materialLocation, potion.unwrapKey().get().identifier());
                    ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, loc);
                    SlotDisplay outputDisplay = new SlotDisplay.ItemStackSlotDisplay(tippedOutput);
                    CraftingRecipe tippedRecipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, outputDisplay)
                            .group(group)
                            .define('a', Ingredient.of(arrow.item().value()), new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(arrow.item(), 1, arrow.components())))
                            .define('l', Ingredient.of(lingeringPotion.item().value()), new SlotDisplay.ItemStackSlotDisplay(lingeringPotion))
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
        registration.registerFromDataComponentTypes(ModItems.ARROW_PLUS.get(), ModDataComponents.ARROW_DATA.get(), ModDataComponents.STICK_DATA.get(), ModDataComponents.FEATHER_DATA.get(), DataComponents.POTION_CONTENTS);
        registration.registerFromDataComponentTypes(ModItems.CUSTOM_FEATHER.get(), ModDataComponents.FEATHER_DATA.get());
        registration.registerFromDataComponentTypes(ModItems.CUSTOM_STICK.get(), ModDataComponents.STICK_DATA.get());
    }

    private static <T> List<ItemStack> partStacks(Holder<T> part, boolean standalone, HolderSet<Item> material, Item customItem, DataComponentType<Holder<T>> component){
        if (standalone){
            return material.stream().map(ItemStack::new).toList();
        }
        ItemStack stack = new ItemStack(customItem);
        stack.set(component, part);
        return List.of(stack);
    }

    public static ItemStackTemplate copyFrom(ItemStackTemplate template){
        return new ItemStackTemplate(template.item(), 8, template.components());
    }

    public static ItemStackTemplate copyFromWithComponents(ItemStackTemplate template, DataComponentPatch patch){
        ItemStack stack = template.apply(patch);
        return new ItemStackTemplate(template.item(), stack.count(), stack.getComponentsPatch());
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addSimpleRecipeManagerPlugin(RecipeTypes.CRAFTING, new ModularArrowRecipeLookup(registration.getJeiHelpers()));
        if (ArrowPlusConfig.hideTippedArrow()){
            IIngredientManager ingredientManager = registration.getJeiHelpers().getIngredientManager();

            RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
            registryAccess.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().filter(reference -> !ArrowPlusConfig.getRemoval().contains(reference.key().identifier().getPath())).forEach(data -> {
                ItemStack output = ArrowParts.createDefault(data, registryAccess, 1);
                List<ItemStack> toRemove = new ArrayList<>();

                registryAccess.lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                    if (!potion.value().getEffects().isEmpty()){
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
