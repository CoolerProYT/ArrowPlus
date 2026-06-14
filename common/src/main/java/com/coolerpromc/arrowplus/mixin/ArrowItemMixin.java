package com.coolerpromc.arrowplus.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(ArrowItem.class)
public class ArrowItemMixin extends Item {
    public ArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltips, TooltipFlag tooltipFlag) {
        Window handle = Minecraft.getInstance().getWindow();
        boolean shiftDown = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);

        if(!shiftDown){
            tooltips.accept(Component.literal("Hold §8[Shift]§r for more info."));
        }
        else{
            boolean infinity = this.asItem() != Items.SPECTRAL_ARROW && this.asItem() != Items.TIPPED_ARROW;
            tooltips.accept(Component.translatable("tooltip.arrowplus.base_damage", "§a2.0"));
            tooltips.accept(Component.translatable("tooltip.arrowplus.flame", "§afalse"));
            tooltips.accept(Component.translatable("tooltip.arrowplus.gravity", "§a0.05"));
            tooltips.accept(Component.translatable("tooltip.arrowplus.infinity", "§a" + infinity));
        }
    }
}
