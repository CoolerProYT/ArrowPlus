package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ModArrowItem extends ArrowItem {
    private final EntityType<? extends AbstractArrow> entityType;

    public ModArrowItem(Properties p_40512_, EntityType<? extends AbstractArrow> entityType) {
        super(p_40512_);
        this.entityType = entityType;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ModArrowEntity(entityType, shooter, level, ammo.copyWithCount(1), weapon, ammo.getOrDefault(ModDataComponents.ARROW_DATA.get(), Holder.direct(ArrowData.EMPTY)).value().baseDamage());
    }

    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        Holder<ArrowData> data = ammo.get(ModDataComponents.ARROW_DATA.get());
        PotionContents potioncontents = ammo.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null){
            return false;
        }
        if (data == null){
            return EnchantmentHelper.getItemEnchantmentLevel(livingEntity.level().registryAccess().getOrThrow(Enchantments.INFINITY), bow) > 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(livingEntity.level().registryAccess().getOrThrow(Enchantments.INFINITY), bow) > 0 && !ArrowPlusConfig.isInfinityBlacklisted(data.unwrapKey().get().identifier().getPath());
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
        arrow.setBaseDamage(stack.getOrDefault(ModDataComponents.ARROW_DATA.get(), Holder.direct(ArrowData.EMPTY)).value().baseDamage());
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltips, TooltipFlag tooltipFlag) {
        boolean shiftDown = InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);

        if(!shiftDown){
            tooltips.accept(Component.literal("Hold §8[Shift]§r for more info."));
        }
        else{
            Holder<ArrowData> data = stack.get(ModDataComponents.ARROW_DATA.get());
            PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
            if (data != null){
                boolean affectedByInfinity = !ArrowPlusConfig.isInfinityBlacklisted(data.unwrapKey().get().identifier().getPath()) && potioncontents == null;
                tooltips.accept(Component.translatable("tooltip.arrowplus.base_damage", "§a" + data.value().baseDamage()));
                tooltips.accept(Component.translatable("tooltip.arrowplus.flame", "§a" + data.value().flame()));
                tooltips.accept(Component.translatable("tooltip.arrowplus.gravity", "§a" + data.value().gravity()));
                tooltips.accept(Component.translatable("tooltip.arrowplus.infinity", "§a" + affectedByInfinity));
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Component arrow = Component.translatable(stack.getOrDefault(ModDataComponents.ARROW_DATA.get(), Holder.direct(ArrowData.EMPTY)).value().translationKey());
        if (stack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
            return Component.translatable("item.arrowplus.tipped", arrow, potionContents.getName("effect.minecraft."));
        }
        return arrow;
    }
}
