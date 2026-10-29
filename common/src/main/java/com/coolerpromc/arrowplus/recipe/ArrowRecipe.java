package com.coolerpromc.arrowplus.recipe;

import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Optional;

@SuppressWarnings({"NullableProblems"})
public class ArrowRecipe extends CustomRecipe {
    public static final ArrowRecipe INSTANCE = new ArrowRecipe();
    public static final MapCodec<ArrowRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArrowRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<ArrowRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private RegistryAccess registryAccess = null;

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        registryAccess = level.registryAccess();
        if (craftingInput.width() == 1 && craftingInput.height() == 3 && craftingInput.ingredientCount() == 3){
            if (isVanillaArrow(craftingInput)){
                return false;
            }
            return ArrowParts.findHead(craftingInput.getItem(0), registryAccess).isPresent()
                    && ArrowParts.findStick(craftingInput.getItem(1), registryAccess).isPresent()
                    && ArrowParts.findFeather(craftingInput.getItem(2), registryAccess).isPresent();
        }
        return false;
    }

    private static boolean isVanillaArrow(CraftingInput craftingInput) {
        return craftingInput.getItem(0).is(Items.FLINT) && craftingInput.getItem(1).is(Items.STICK) && craftingInput.getItem(2).is(Items.FEATHER);
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        if (registryAccess == null || craftingInput.width() != 1 || craftingInput.height() != 3){
            return ItemStack.EMPTY;
        }
        Optional<Holder.Reference<ArrowData>> head = ArrowParts.findHead(craftingInput.getItem(0), registryAccess);
        Optional<Holder<StickData>> stick = ArrowParts.findStick(craftingInput.getItem(1), registryAccess);
        Optional<Holder<FeatherData>> feather = ArrowParts.findFeather(craftingInput.getItem(2), registryAccess);
        if (head.isEmpty() || stick.isEmpty() || feather.isEmpty()){
            return ItemStack.EMPTY;
        }
        return ArrowParts.create(head.get(), stick, feather, head.get().value().outputAmount());
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializer.ARROW_RECIPE_SERIALIZER.get();
    }
}
