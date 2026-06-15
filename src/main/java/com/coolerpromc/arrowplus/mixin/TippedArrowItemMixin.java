package com.coolerpromc.arrowplus.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin extends Item {
    public TippedArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (this == Items.TIPPED_ARROW){
            if(!tooltipFlag.hasShiftDown()){
                tooltips.add(Component.literal("Hold §8[Shift]§r for more info."));
            }
            else{
                tooltips.add(Component.translatable("tooltip.arrowplus.base_damage", "§a2.0"));
                tooltips.add(Component.translatable("tooltip.arrowplus.flame", "§afalse"));
                tooltips.add(Component.translatable("tooltip.arrowplus.gravity", "§a0.05"));
                tooltips.add(Component.translatable("tooltip.arrowplus.infinity", "§afalse"));
            }
        }
    }
}
