package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.coolerpromc.fletchingrecipe.compat.arrowplus.ArrowPlusTippedRecipe", remap = false)
public abstract class FletchingRecipeArrowItemMixin {
    @Redirect(method = "*", at = @At(value = "FIELD",
            target = "Lcom/coolerpromc/arrowplus/item/ModItems;ARROW_PLUS:Lcom/coolerpromc/arrowplus/platform/util/RegistryHandler;"),
            require = 0)
    private static RegistryHandler<Item, ModArrowItem> arrowplus$compatibleArrowItem() {
        return ModItems.ARROW_PLUS;
    }
}
