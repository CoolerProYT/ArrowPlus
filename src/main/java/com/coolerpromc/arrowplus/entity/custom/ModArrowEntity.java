package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends AbstractArrow {
    private final ItemStack stack;
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ModArrowEntity.class, EntityDataSerializers.INT);

    public ModArrowEntity(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateColor();
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, double baseDamage) {
        super(entityType, owner, level);
        this.stack = pickupItemStack;
        this.pickup = Pickup.ALLOWED;
        ItemStack firedFromWeapon = owner.getUseItem();

        if (firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = firedFromWeapon.getEnchantmentLevel(Enchantments.POWER_ARROWS);
            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = firedFromWeapon.getEnchantmentLevel(Enchantments.INFINITY_ARROWS);
            this.pickup = infinityLevel > 0 ? Pickup.DISALLOWED : Pickup.ALLOWED;
        }
        this.setBaseDamage(baseDamage);
        this.updateColor();
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level);
        this.stack = pickupItemStack;
        this.updateColor();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("color", this.getColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(COLOR, tag.getInt("color"));
    }

    @Override
    protected ItemStack getPickupItem() {
        return stack;
    }

    public void updateColor() {
        this.entityData.set(COLOR, ArrowData.load(stack.getOrCreateTag()).color());
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }
}
