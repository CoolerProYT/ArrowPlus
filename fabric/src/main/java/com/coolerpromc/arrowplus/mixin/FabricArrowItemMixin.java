package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(ArrowItem.class)
public abstract class FabricArrowItemMixin extends Item implements InfiniteArrow {
    public FabricArrowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isInfinite(ItemStack arrowStack, ItemStack bowStack, LivingEntity shooter) {
        return false;
    }
}
