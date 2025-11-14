package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.util.InfiniteArrow;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ModArrowItem extends ArrowItem implements InfiniteArrow {
    public ModArrowItem(Item.Settings p_40512_, EntityType<? extends PersistentProjectileEntity> entityType) {
        super(p_40512_);
    }

    @Override
    public PersistentProjectileEntity createArrow(World level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ModArrowEntity(shooter, level, ammo.copyWithCount(1), weapon, ammo.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value().baseDamage());
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        if (livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.POWER).isPresent())
            return EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.POWER).orElseThrow(), bow) > 0;
        return false;
    }

    @Override
    public ProjectileEntity createEntity(World level, Position location, ItemStack stack, Direction p_338469_) {
        ModArrowEntity arrow = new ModArrowEntity(
                location.getX(),
                location.getY(),
                location.getZ(),
                level,
                stack.copyWithCount(1),
                null
        );
        arrow.setDamage(stack.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value().baseDamage());
        arrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrow;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.arrowplus.base_damage", stack.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value().baseDamage()).withColor(0xBBBBBB));
        PotionContentsComponent potioncontents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potioncontents != null) {
            Objects.requireNonNull(tooltip);
            potioncontents.buildTooltip(tooltip::add, 0.125F, context.getUpdateTickRate());
        }

    }

    @Override
    public Text getName(ItemStack stack) {
        Text arrow = Text.translatable(stack.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value().translationKey());
        if (stack.contains(DataComponentTypes.POTION_CONTENTS)){
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if (potionContents.potion().isPresent())
                return Text.translatable("item.arrowplus.tipped", arrow, Text.translatable("effect.minecraft." + potionContents.potion().get().getKey().get().getValue().getPath()));
        }
        return arrow;
    }
}
