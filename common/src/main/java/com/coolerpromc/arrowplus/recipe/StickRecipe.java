package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"NullableProblems"})
public class StickRecipe extends CustomRecipe {
    public static final StickRecipe INSTANCE = new StickRecipe();
    public static final MapCodec<StickRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, StickRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<StickRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private Holder<StickData> resultData = Holder.direct(StickData.EMPTY);

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.width() == 1 && craftingInput.height() == 2 && craftingInput.ingredientCount() == 2){
            Set<Item> material = new HashSet<>();
            material.add(craftingInput.getItem(0).getItem());
            material.add(craftingInput.getItem(1).getItem());

            for (Holder<StickData> stickData : level.registryAccess().lookupOrThrow(ModRegistries.STICK_DATA_KEY).listElements().toList()){
                if (material.size() == 1 && stickData.value().ingredient().test(material.iterator().next().getDefaultInstance())){
                    this.resultData = stickData;
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
        ItemStack stick = new ItemStack(ModItems.CUSTOM_STICK.get(), this.resultData.value().outputAmount());
        stick.set(ModDataComponents.STICK_DATA.get(), this.resultData);
        return stick;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializer.STICK_RECIPE_SERIALIZER.get();
    }
}