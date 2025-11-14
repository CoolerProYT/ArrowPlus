package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.arrow.ArrowData;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends PersistentProjectileEntity {
    private final ItemStack stack;
    private static final TrackedData<ArrowData> ARROW_DATA = DataTracker.registerData(ModArrowEntity.class, ModEntities.ARROW_DATA);
    private static final TrackedData<Integer> ID_EFFECT_COLOR  = DataTracker.registerData(ModArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public ModArrowEntity(EntityType<? extends PersistentProjectileEntity> p_331098_, World p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(LivingEntity owner, World level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon, double baseDamage) {
        super(ModEntities.ARROW_PLUS, owner, level, pickupItemStack.copyWithCount(1), firedFromWeapon);
        this.stack = pickupItemStack;
        this.pickupType = PickupPermission.ALLOWED;

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = EnchantmentHelper.getLevel(level.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.POWER).orElseThrow(), firedFromWeapon);

            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = EnchantmentHelper.getLevel(level.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.INFINITY).orElseThrow(), firedFromWeapon);
            this.pickupType = infinityLevel > 0 ? PickupPermission.DISALLOWED : PickupPermission.ALLOWED;
        }
        this.setDamage(baseDamage);
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(double x, double y, double z, World level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.ARROW_PLUS, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.stack = pickupItemStack;
        this.updateArrowData();
        this.updateColor();
    }

    private PotionContentsComponent getPotionContents() {
        return this.stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    @Override
    protected void setStack(ItemStack stack) {
        super.setStack(stack);
        this.updateColor();
    }

    private void updateColor() {
        PotionContentsComponent potioncontents = this.getPotionContents();
        this.dataTracker.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContentsComponent.DEFAULT) ? -1 : potioncontents.getColor());
    }

    @Override
    protected @NotNull ItemStack getDefaultItemStack() {
        return stack;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ARROW_DATA, ArrowData.EMPTY);
        builder.add(ID_EFFECT_COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(1);
                }
            } else {
                this.spawnParticles(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContentsComponent.DEFAULT) && this.inGroundTime >= 600) {
            this.getWorld().sendEntityStatus(this, (byte)0);
            this.setStack(new ItemStack(Items.ARROW));
        }
    }

    private void spawnParticles(int amount) {
        int i = this.getColor();
        if (i != -1 && amount > 0) {
            for(int j = 0; j < amount; ++j) {
                this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, i), this.getParticleX((double)0.5F), this.getRandomBodyY(), this.getParticleZ((double)0.5F), (double)0.0F, (double)0.0F, (double)0.0F);
            }

        }
    }

    public int getColor() {
        return this.dataTracker.get(ID_EFFECT_COLOR);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("arrow_data", ArrowData.CODEC.encodeStart(NbtOps.INSTANCE, this.getArrowData()).getOrThrow());
        nbt.putInt("color", this.getColor());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(ARROW_DATA, ArrowData.CODEC.parse(NbtOps.INSTANCE, nbt.get("arrow_data")).getOrThrow());
        this.dataTracker.set(ID_EFFECT_COLOR, nbt.getInt("color"));
    }

    public void updateArrowData() {
        this.dataTracker.set(ARROW_DATA, stack.getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value());
    }

    public ArrowData getArrowData(){
        return this.dataTracker.get(ARROW_DATA);
    }

    @Override
    public Text getName() {
        return Text.translatable(getArrowData().translationKey());
    }

    @Override
    protected double getGravity() {
        return getArrowData().gravity();
    }

    @Override
    protected void onHit(LivingEntity livingEntity) {
        super.onHit(livingEntity);
        getArrowData().effects().forEach((resourceLocation, integer) -> Registries.POTION.getEntry(resourceLocation).ifPresent(potionReference -> potionReference.value().getEffects().forEach(instance -> livingEntity.addStatusEffect(
                new StatusEffectInstance(instance.getEffectType(), integer, instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon(), null)
        ))));
        Entity entity = this.getEffectCause();
        PotionContentsComponent potionContentsComponent = this.getPotionContents();
        if (potionContentsComponent.potion().isPresent()) {
            for(StatusEffectInstance statusEffectInstance : ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).getEffects()) {
                livingEntity.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.mapDuration((i) -> i / 8), 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
            }
        }

        for(StatusEffectInstance statusEffectInstance : potionContentsComponent.customEffects()) {
            livingEntity.addStatusEffect(statusEffectInstance, entity);
        }
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame();
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float g = (float)(i >> 8 & 255) / 255.0F;
                float h = (float)(i >> 0 & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h), this.getParticleX((double)0.5F), this.getRandomBodyY(), this.getParticleZ((double)0.5F), (double)0.0F, (double)0.0F, (double)0.0F);
                }
            }
        } else {
            super.handleStatus(status);
        }

    }
}
