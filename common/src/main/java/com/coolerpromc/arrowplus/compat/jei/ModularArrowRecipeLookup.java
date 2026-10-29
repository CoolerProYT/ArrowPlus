package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.ModItems;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ModularArrowRecipeLookup implements ISimpleRecipeManagerPlugin<RecipeHolder<CraftingRecipe>> {
    private final IJeiHelpers helpers;

    public ModularArrowRecipeLookup(IJeiHelpers helpers) {
        this.helpers = helpers;
    }

    private boolean handles(ItemStack stack) {
        if (!stack.is(ModItems.ARROW_PLUS.get()) || Minecraft.getInstance().level == null){
            return false;
        }
        Holder<ArrowData> head = stack.get(ModDataComponents.ARROW_DATA.get());
        if (head == null || head.unwrapKey().isEmpty() || ArrowPlusConfig.getRemoval().contains(head.unwrapKey().get().identifier().getPath())){
            return false;
        }
        RegistryAccess registries = Minecraft.getInstance().level.registryAccess();

        return !Objects.equals(stack.get(ModDataComponents.STICK_DATA.get()), ArrowParts.defaultStick(registries).orElse(null))
                || !Objects.equals(stack.get(ModDataComponents.FEATHER_DATA.get()), ArrowParts.defaultFeather(registries).orElse(null));
    }

    @Override
    public boolean isHandledInput(ITypedIngredient<?> input) {
        return input.getItemStack().filter(this::handles).filter(s -> !s.has(DataComponents.POTION_CONTENTS)).isPresent();
    }

    @Override
    public boolean isHandledOutput(ITypedIngredient<?> output) {
        return output.getItemStack().filter(this::handles).isPresent();
    }

    @Override
    public List<RecipeHolder<CraftingRecipe>> getRecipesForInput(ITypedIngredient<?> input) {
        if (!isHandledInput(input)){
            return List.of();
        }
        ItemStack arrow = input.getItemStack().orElseThrow();
        return Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.POTION).listElements()
                .filter(p -> !p.value().getEffects().isEmpty())
                .map(p -> tipped(arrow, new PotionContents(p)))
                .toList();
    }

    @Override
    public List<RecipeHolder<CraftingRecipe>> getRecipesForOutput(ITypedIngredient<?> output) {
        if (!isHandledOutput(output)){
            return List.of();
        }
        ItemStack arrow = output.getItemStack().orElseThrow();
        PotionContents potion = arrow.get(DataComponents.POTION_CONTENTS);
        return List.of(potion == null ? assembly(arrow) : tipped(arrow, potion));
    }

    private RecipeHolder<CraftingRecipe> assembly(ItemStack arrow) {
        Holder<ArrowData> head = arrow.get(ModDataComponents.ARROW_DATA.get());
        Holder<StickData> stick = arrow.get(ModDataComponents.STICK_DATA.get());
        Holder<FeatherData> feather = arrow.get(ModDataComponents.FEATHER_DATA.get());
        ItemStack result = ArrowParts.create(head, Optional.ofNullable(stick), Optional.ofNullable(feather), head.value().outputAmount());
        ItemStack stickStack = part(stick, ArrowParts.stick(arrow).standalone(), ArrowParts.stick(arrow).material(), ModItems.CUSTOM_STICK.get(), ModDataComponents.STICK_DATA.get());
        ItemStack featherStack = part(feather, ArrowParts.feather(arrow).standalone(), ArrowParts.feather(arrow).material(), ModItems.CUSTOM_FEATHER.get(), ModDataComponents.FEATHER_DATA.get());
        CraftingRecipe recipe = this.helpers.getVanillaRecipeFactory().createShapedRecipeBuilder(CraftingBookCategory.MISC, display(result))
                .define('h', head.value().ingredient())
                .define('s', Ingredient.of(stickStack.getItem()), display(stickStack))
                .define('f', Ingredient.of(featherStack.getItem()), display(featherStack))
                .pattern("h")
                .pattern("s")
                .pattern("f")
                .build();
        return holder(arrow, "assembly", recipe);
    }

    private RecipeHolder<CraftingRecipe> tipped(ItemStack arrow, PotionContents potion) {
        ItemStack input = arrow.copyWithCount(1);
        input.remove(DataComponents.POTION_CONTENTS);
        ItemStack result = input.copyWithCount(8);
        result.set(DataComponents.POTION_CONTENTS, potion);
        ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
        lingering.set(DataComponents.POTION_CONTENTS, potion);
        CraftingRecipe recipe = this.helpers.getVanillaRecipeFactory().createShapedRecipeBuilder(CraftingBookCategory.MISC, display(result))
                .define('a', Ingredient.of(ModItems.ARROW_PLUS.get()), display(input))
                .define('p', Ingredient.of(Items.LINGERING_POTION), display(lingering))
                .pattern("aaa")
                .pattern("apa")
                .pattern("aaa")
                .build();
        return holder(result, "tipped", recipe);
    }

    private static SlotDisplay display(ItemStack stack) {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(stack));
    }

    private static <T> ItemStack part(Holder<T> data, boolean standalone, HolderSet<Item> materials, Item custom, DataComponentType<Holder<T>> component) {
        ItemStack stack = new ItemStack(standalone ? materials.get(0).value() : custom);
        if (!standalone && data != null){
            stack.set(component, data);
        }
        return stack;
    }

    private static RecipeHolder<CraftingRecipe> holder(ItemStack stack, String kind, CraftingRecipe recipe) {
        Identifier id = Constants.id("jei/dynamic/" + kind + "/" + Integer.toUnsignedString(ItemStack.hashItemAndComponents(stack)));
        return new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), recipe);
    }

    @Override
    public List<RecipeHolder<CraftingRecipe>> getAllRecipes() {
        return List.of();
    }
}
