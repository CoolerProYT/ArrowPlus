package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.entity.custom.ModArrowEntity;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArrowItem extends ArrowItem {
    private final RegistryObject<EntityType<ModArrowEntity>> entityType;

    public ModArrowItem(Properties p_40512_, RegistryObject<EntityType<ModArrowEntity>> entityType) {
        super(p_40512_);
        this.entityType = entityType;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter) {
        return new ModArrowEntity(entityType.get(), shooter, level, ammo.copyWithCount(1), ArrowData.load(ammo.getOrCreateTag(), level.registryAccess()).baseDamage());
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;
    }

    @Override
    public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
        ModArrowEntity arrow = new ModArrowEntity(
                entityType.get(),
                location.getX(),
                location.getY(),
                location.getZ(),
                level,
                stack.copyWithCount(1),
                null
        );
        arrow.setBaseDamage(ArrowData.load(stack.getOrCreateTag(), level.registryAccess()).baseDamage());
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag p_41424_) {
        if (level == null) return;
        ArrowData data = ArrowData.load(stack.getOrCreateTag(), level.registryAccess());
        if (data != null){
            tooltipComponents.set(0, Component.translatable(data.translationKey()));
            tooltipComponents.add(Component.translatable("tooltip.arrowplus.base_damage", data.baseDamage()).withStyle(Style.EMPTY.withColor(0xBBBBBB)));
        }
    }
}
