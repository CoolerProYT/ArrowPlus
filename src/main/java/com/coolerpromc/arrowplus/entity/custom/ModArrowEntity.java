package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.coolerpromc.arrowplus.arrow.ArrowData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<ArrowData> ARROW_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.ARROW_DATA);
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR  = SynchedEntityData.defineId(ModArrowEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> PICKUP_STACK  = SynchedEntityData.defineId(ModArrowEntity.class, EntityDataSerializers.ITEM_STACK);

    public ModArrowEntity(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.setPickupItemStack(pickupItemStack);
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon, double baseDamage) {
        super(ModEntities.ARROW_PLUS, owner, level, pickupItemStack.copyWithCount(1), firedFromWeapon);
        this.setPickupItemStack(pickupItemStack);
        this.pickup = Pickup.ALLOWED;

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.POWER), firedFromWeapon);

            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.INFINITY), firedFromWeapon);
            this.pickup = infinityLevel > 0 ? Pickup.DISALLOWED : Pickup.ALLOWED;
        }
        this.setBaseDamage(baseDamage);
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.ARROW_PLUS, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setPickupItemStack(pickupItemStack);
        this.updateArrowData();
        this.updateColor();
    }

    private PotionContents getPotionContents() {
        return this.entityData.get(PICKUP_STACK).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private float getPotionDurationScale() {
        return this.entityData.get(PICKUP_STACK).getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F);
    }

    @Override
    protected void setPickupItemStack(ItemStack stack) {
        super.setPickupItemStack(stack);
        this.updateColor();
        this.entityData.set(PICKUP_STACK, stack);
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.entityData.get(PICKUP_STACK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ARROW_DATA, ArrowData.EMPTY);
        builder.define(ID_EFFECT_COLOR, -1);
        builder.define(PICKUP_STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.isInGround()) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.isInGround() && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
            this.level().broadcastEntityEvent(this, (byte)0);
            this.setPickupItemStack(new ItemStack(Items.ARROW));
        }
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != -1 && particleAmount > 0) {
            for(int j = 0; j < particleAmount; ++j) {
                this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i), this.getRandomX(0.5F), this.getRandomY(), this.getRandomZ(0.5F), 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("arrow_data", ArrowData.CODEC, this.getArrowData());
        valueOutput.putInt("color", this.getColor());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        valueInput.read("arrow_data", ArrowData.CODEC).ifPresent(arrowData -> this.entityData.set(ARROW_DATA, arrowData));
        this.entityData.set(ID_EFFECT_COLOR, valueInput.getIntOr("color", -1));
    }

    public void updateArrowData() {
        this.entityData.set(ARROW_DATA, this.entityData.get(PICKUP_STACK).getOrDefault(ModDataComponents.ARROW_DATA, Holder.direct(ArrowData.EMPTY)).value());
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
    protected void doPostHurtEffects(LivingEntity livingEntity) {
        super.doPostHurtEffects(livingEntity);
        getArrowData().effects().forEach((resourceLocation, integer) -> BuiltInRegistries.POTION.get(resourceLocation).ifPresent(potionReference -> potionReference.value().getEffects().forEach(instance -> livingEntity.addEffect(
                new MobEffectInstance(instance.getEffect(), integer, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon(), null)
        ))));
        Entity entity = this.getEffectSource();
        PotionContents potioncontents = this.getPotionContents();
        float f = this.getPotionDurationScale();
        potioncontents.forEachEffect((p_478604_) -> livingEntity.addEffect(p_478604_, entity), f);
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame() || super.isOnFire();
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getRandomX(0.5F), this.getRandomY(), this.getRandomZ(0.5F), 0.0F, 0.0F, 0.0F);
                }
            }
        } else {
            super.handleEntityEvent(b);
        }
    }
}
