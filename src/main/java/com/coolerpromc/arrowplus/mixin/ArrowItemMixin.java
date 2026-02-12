package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.InfiniteArrow;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

@Mixin(ArrowItem.class)
public class ArrowItemMixin extends Item implements InfiniteArrow {
    public ArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("tooltip.arrowplus.base_damage", 2.0d).withColor(0xBBBBBB));
    }

    @Override
    public boolean isInfinite(ItemStack arrowStack, ItemStack bowStack, LivingEntity shooter) {
        return false;
    }
}
