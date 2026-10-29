package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.item.PartTooltips;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ModArrowItem extends ArrowItem {
    private final EntityType<? extends AbstractArrow> entityType;

    public ModArrowItem(Properties p_40512_, EntityType<? extends AbstractArrow> entityType) {
        super(p_40512_);
        this.entityType = entityType;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ModArrowEntity(entityType, shooter, level, ammo.copyWithCount(1), weapon, ArrowParts.head(ammo).baseDamage());
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        if (!stack.has(ModDataComponents.ARROW_DATA.get())){
            PotionContents potion = stack.get(DataComponents.POTION_CONTENTS);
            ItemStack vanilla = new ItemStack(potion != null ? Items.TIPPED_ARROW : Items.ARROW);
            vanilla.set(DataComponents.POTION_CONTENTS, potion);
            ArrowParts.replaceInvalid(stack, owner, vanilla);
            return;
        }
        ArrowParts.migrate(stack, level.registryAccess());
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
        arrow.setBaseDamage(ArrowParts.head(stack).baseDamage());
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltips, TooltipFlag tooltipFlag) {
        Holder<ArrowData> data = stack.get(ModDataComponents.ARROW_DATA.get());
        if (data != null){
            boolean infinity = !ArrowPlusConfig.isInfinityBlacklisted(data.unwrapKey().get().identifier().getPath())
                    && !stack.has(DataComponents.POTION_CONTENTS);
            PartTooltips.addArrow(data.value(), ArrowParts.stick(stack), ArrowParts.feather(stack), infinity, tooltips);
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Component arrow = Component.translatable(ArrowParts.head(stack).translationKey());
        if (stack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
            return Component.translatable("item.arrowplus.tipped", arrow, potionContents.getName("effect.minecraft."));
        }
        return arrow;
    }
}
