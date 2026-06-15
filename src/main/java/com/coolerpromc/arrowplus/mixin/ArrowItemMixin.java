package com.coolerpromc.arrowplus.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ArrowItem.class)
public class ArrowItemMixin extends Item {
    public ArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag tooltipFlag) {
        if (this == Items.SPECTRAL_ARROW || this == Items.ARROW || this == Items.TIPPED_ARROW){
            if(!tooltipFlag.hasShiftDown()){
                tooltips.add(Component.literal("Hold §8[Shift]§r for more info."));
            }
            else{
                boolean infinity = this.asItem() != Items.SPECTRAL_ARROW && this.asItem() != Items.TIPPED_ARROW;
                tooltips.add(Component.translatable("tooltip.arrowplus.base_damage", "§a2.0"));
                tooltips.add(Component.translatable("tooltip.arrowplus.flame", "§afalse"));
                tooltips.add(Component.translatable("tooltip.arrowplus.gravity", "§a0.05"));
                tooltips.add(Component.translatable("tooltip.arrowplus.infinity", "§a" + infinity));
            }
        }
    }
}
