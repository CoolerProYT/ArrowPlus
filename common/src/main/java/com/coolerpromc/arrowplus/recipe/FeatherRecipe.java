package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"NullableProblems"})
public class FeatherRecipe extends CustomRecipe {
    public static final FeatherRecipe INSTANCE = new FeatherRecipe();
    public static final MapCodec<FeatherRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, FeatherRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<FeatherRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private Holder<FeatherData> resultData = Holder.direct(FeatherData.EMPTY);

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.width() == 3 && craftingInput.height() == 3 && craftingInput.ingredientCount() == 5){
            ItemStack feather = craftingInput.getItem(1, 1);
            Set<Item> material = new HashSet<>();
            material.add(craftingInput.getItem(0, 1).getItem());
            material.add(craftingInput.getItem(1, 0).getItem());
            material.add(craftingInput.getItem(1, 2).getItem());
            material.add(craftingInput.getItem(2, 1).getItem());

            for (Holder<FeatherData> featherData : level.registryAccess().lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).listElements().toList()){
                if (material.size() == 1 && featherData.value().ingredient().test(material.iterator().next().getDefaultInstance()) && feather.is(Items.FEATHER)){
                    this.resultData = featherData;
                    return true;
                }
            }
            return false;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        ItemStack feather = new ItemStack(ModItems.CUSTOM_FEATHER.get(), this.resultData.value().outputAmount());
        feather.set(ModDataComponents.FEATHER_DATA.get(), this.resultData);
        return feather;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializer.FEATHER_RECIPE_SERIALIZER.get();
    }
}