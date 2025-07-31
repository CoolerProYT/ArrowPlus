package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ArrowItem.class)
public class ArrowItemMixin extends Item implements InfiniteArrow {
    public ArrowItemMixin(Settings properties) {
        super(properties);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.arrowplus.base_damage", 2.0d).fillStyle(Style.EMPTY.withColor(0xBBBBBB)));
    }

    @Override
    public boolean isInfinite(ItemStack arrowStack, ItemStack bowStack, LivingEntity shooter) {
        return false;
    }
}
