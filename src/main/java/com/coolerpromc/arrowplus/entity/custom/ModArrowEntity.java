package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends AbstractArrow {
    private final ItemStack stack;
    private static final EntityDataAccessor<ArrowData> ARROW_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.ARROW_DATA.get());

    public ModArrowEntity(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateArrowData();
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
        this.updateArrowData();
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level);
        this.stack = pickupItemStack;
        this.updateArrowData();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARROW_DATA, ArrowData.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.getArrowData().save(tag, this.level().registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.getArrowData() != null){
            this.entityData.set(ARROW_DATA, ArrowData.load(tag, this.level().registryAccess()));
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return stack;
    }

    public void updateArrowData(){
        this.entityData.set(ARROW_DATA, ArrowData.load(stack.getOrCreateTag(), this.level().registryAccess()));
    }

    public ArrowData getArrowData(){
        return this.entityData.get(ARROW_DATA);
    }

    @Override
    public Component getName() {
        return Component.translatable(getArrowData().translationKey());
    }

    @Override
    public void tick() {
        if (this.inGround) {
            super.tick();
            return;
        }

        Vec3 motion = this.getDeltaMovement();
        double gravity = this.getArrowData().gravity();

        if (!this.isNoGravity()) {
            motion = motion.add(0.0D, -gravity, 0.0D);
        }

        this.setDeltaMovement(motion);
        super.tick();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        getArrowData().effects().forEach((resourceLocation, integer) -> BuiltInRegistries.POTION.getOptional(resourceLocation).ifPresent(potionReference -> potionReference.getEffects().forEach(instance -> entity.addEffect(
                new MobEffectInstance(instance.getEffect(), integer, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon())
        ))));
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame();
    }
}
