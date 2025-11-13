package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArrowItem extends ArrowItem {
    private final EntityType<? extends AbstractArrow> entityType;

    public ModArrowItem(Properties p_40512_, EntityType<? extends AbstractArrow> entityType) {
        super(p_40512_);
        this.entityType = entityType;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ModArrowEntity(entityType, shooter, level, ammo.copyWithCount(1), weapon, ammo.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value().baseDamage());
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return bow.getEnchantmentLevel(livingEntity.level().registryAccess().holderOrThrow(Enchantments.INFINITY)) > 0;
    }

    @Override
    public Projectile asProjectile(Level level, Position location, ItemStack stack, Direction p_338469_) {
        ModArrowEntity arrow = new ModArrowEntity(
                entityType,
                location.x(),
                location.y(),
                location.z(),
                level,
                stack.copyWithCount(1),
                null
        );
        arrow.setBaseDamage(stack.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value().baseDamage());
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.arrowplus.base_damage", stack.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value().baseDamage()).withColor(0xBBBBBB));

    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(stack.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value().translationKey());
    }
}
