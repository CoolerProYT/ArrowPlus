package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
        Holder<ArrowData> data = ammo.get(ModDataComponents.ARROW_DATA.get());
        if (data == null){
            return bow.getEnchantmentLevel(livingEntity.level().registryAccess().holderOrThrow(Enchantments.INFINITY)) > 0;
        }
        return bow.getEnchantmentLevel(livingEntity.level().registryAccess().holderOrThrow(Enchantments.INFINITY)) > 0 && !ArrowPlusConfig.CONFIG.isInfinityBlacklisted(data.getKey().location().getPath());
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
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            Objects.requireNonNull(tooltipComponents);
            potioncontents.addPotionTooltip(tooltipComponents::add, 0.125F, context.tickRate());
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Component arrow = Component.translatable(stack.getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value().translationKey());
        if (stack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
            if (potionContents.potion().isPresent())
                return Component.translatable("item.arrowplus.tipped", arrow, Component.translatable("effect.minecraft." + potionContents.potion().get().getKey().location().getPath()));
        }
        return arrow;
    }
}
