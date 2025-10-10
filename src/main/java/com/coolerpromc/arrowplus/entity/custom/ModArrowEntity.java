package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends AbstractArrow {
    private final ItemStack stack;
    private static final EntityDataAccessor<ArrowData> ARROW_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.ARROW_DATA.get());

    public ModArrowEntity(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateArrowData();
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon, double baseDamage) {
        super(entityType, owner, level, pickupItemStack.copyWithCount(1), firedFromWeapon);
        this.stack = pickupItemStack;
        this.pickup = Pickup.ALLOWED;

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = firedFromWeapon.getEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.POWER));
            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = firedFromWeapon.getEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.INFINITY));
            this.pickup = infinityLevel > 0 ? Pickup.DISALLOWED : Pickup.ALLOWED;
        }
        this.setBaseDamage(baseDamage);
        this.updateArrowData();
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.stack = pickupItemStack;
        this.updateArrowData();
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return stack;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ARROW_DATA, ArrowData.EMPTY);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("arrow_data", ArrowData.CODEC, this.getArrowData());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        valueInput.read("arrow_data", ArrowData.CODEC).ifPresent(arrowData -> this.entityData.set(ARROW_DATA, arrowData));
    }

    public void updateArrowData(){
        this.entityData.set(ARROW_DATA, stack.getOrDefault(ModDataComponents.ARROW_DATA, ArrowData.EMPTY));
    }

    public ArrowData getArrowData(){
        return this.entityData.get(ARROW_DATA);
    }

    @Override
    public Component getName() {
        return Component.translatable(getArrowData().translationKey());
    }

    @Override
    protected double getDefaultGravity() {
        return getArrowData().gravity();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        getArrowData().effects().forEach((resourceLocation, integer) -> BuiltInRegistries.POTION.get(resourceLocation).ifPresent(potionReference -> potionReference.value().getEffects().forEach(instance -> entity.addEffect(
                new MobEffectInstance(instance.getEffect(), integer, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon(), null)
        ))));
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame();
    }
}
