package com.coolerpromc.arrowplus.mixin;

import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

@Pseudo
@Mixin(targets = "com.coolerpromc.fletchingrecipe.compat.jei.ModJEIPlugin", remap = false)
public abstract class FletchingJeiSubtypeMixin {
    @Redirect(method = "registerItemSubtypes", at = @At(value = "INVOKE",
            target = "Lmezz/jei/api/registration/ISubtypeRegistration;registerFromDataComponentTypes(Lnet/minecraft/world/item/Item;[Lnet/minecraft/core/component/DataComponentType;)V"))
    private static void arrowplus$includePotion(ISubtypeRegistration registration, Item item, DataComponentType<?>[] components) {
        if (item == Items.TIPPED_ARROW){
            components = Stream.concat(Stream.of(DataComponents.POTION_CONTENTS), Stream.of(components))
                    .distinct().toArray(DataComponentType<?>[]::new);
        }
        registration.registerFromDataComponentTypes(item, components);
    }
}
