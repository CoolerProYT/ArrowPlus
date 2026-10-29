package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.platform.Services;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "mezz.jei.library.plugins.vanilla.VanillaPlugin", remap = false)
public abstract class JeiVanillaArrowSubtypeMixin {
    @Redirect(method = "registerItemSubtypes", at = @At(value = "INVOKE",
            target = "Lmezz/jei/api/registration/ISubtypeRegistration;registerSubtypeInterpreter(Lnet/minecraft/world/item/Item;Lmezz/jei/api/ingredients/subtypes/ISubtypeInterpreter;)V"))
    private static void arrowplus$deferTippedArrow(ISubtypeRegistration registration, Item item, ISubtypeInterpreter<ItemStack> interpreter) {
        if (item != Items.TIPPED_ARROW || !Services.PLATFORM.isModLoaded("fletchingrecipe")){
            registration.registerSubtypeInterpreter(item, interpreter);
        }
    }
}
