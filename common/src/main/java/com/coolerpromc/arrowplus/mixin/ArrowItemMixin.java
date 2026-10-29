package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.item.PartTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(ArrowItem.class)
public class ArrowItemMixin extends Item {
    public ArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltips, TooltipFlag tooltipFlag) {
        if (this == Items.SPECTRAL_ARROW || this == Items.ARROW || this == Items.TIPPED_ARROW){
            boolean infinity = this.asItem() == Items.ARROW;
            PartTooltips.addVanillaArrow(infinity, tooltips);
        }
    }
}
