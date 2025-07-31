package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.util.ArrowData;
import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArrowItem extends ArrowItem implements InfiniteArrow {
    public ModArrowItem(Item.Settings p_40512_, EntityType<? extends PersistentProjectileEntity> entityType) {
        super(p_40512_);
    }

    @Override
    public PersistentProjectileEntity createArrow(World level, ItemStack ammo, LivingEntity shooter) {
        return new ModArrowEntity(shooter, level, ammo.copyWithCount(1), ArrowData.load(ammo.getOrCreateNbt()).baseDamage());
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return EnchantmentHelper.getLevel(Enchantments.INFINITY, bow) > 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.arrowplus.base_damage", ArrowData.load(stack.getOrCreateNbt()).baseDamage()).fillStyle(Style.EMPTY.withColor(0xBBBBBB)));
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(ArrowData.load(stack.getOrCreateNbt()).translationKey());
    }
}
