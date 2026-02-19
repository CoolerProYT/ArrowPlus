package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.arrow.ArrowData;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends PersistentProjectileEntity {
    private static final TrackedData<ArrowData> ARROW_DATA = DataTracker.registerData(ModArrowEntity.class, ModEntities.ARROW_DATA);
    private static final TrackedData<Integer> ID_EFFECT_COLOR  = DataTracker.registerData(ModArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<ItemStack> PICKUP_STACK  = DataTracker.registerData(ModArrowEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public ModArrowEntity(EntityType<? extends PersistentProjectileEntity> p_331098_, World p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.setStack(pickupItemStack);
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(LivingEntity owner, World level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon, double baseDamage) {
        super(ModEntities.ARROW_PLUS, owner, level, pickupItemStack.copyWithCount(1), firedFromWeapon);
        this.setStack(pickupItemStack);
        this.pickupType = PickupPermission.ALLOWED;

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = EnchantmentHelper.getLevel(level.getRegistryManager().getEntryOrThrow(Enchantments.POWER), firedFromWeapon);

            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = EnchantmentHelper.getLevel(level.getRegistryManager().getEntryOrThrow(Enchantments.INFINITY), firedFromWeapon);
            this.pickupType = infinityLevel > 0 ? PickupPermission.DISALLOWED : PickupPermission.ALLOWED;
        }
        this.setDamage(baseDamage);
        this.updateArrowData();
        this.updateColor();
    }

    public ModArrowEntity(double x, double y, double z, World level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.ARROW_PLUS, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setStack(pickupItemStack);
        this.updateArrowData();
        this.updateColor();
    }

    private PotionContentsComponent getPotionContents() {
        return this.dataTracker.get(PICKUP_STACK).getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    private float getPotionDurationScale() {
        return this.dataTracker.get(PICKUP_STACK).getOrDefault(DataComponentTypes.POTION_DURATION_SCALE, 1.0F);
    }

    @Override
    protected void setStack(ItemStack stack) {
        super.setStack(stack);
        this.updateColor();
        this.dataTracker.set(PICKUP_STACK, stack);
    }

    private void updateColor() {
        PotionContentsComponent potioncontents = this.getPotionContents();
        this.dataTracker.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContentsComponent.DEFAULT) ? -1 : potioncontents.getColor());
    }

    @Override
    protected @NotNull ItemStack getDefaultItemStack() {
        return this.dataTracker.get(PICKUP_STACK);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ARROW_DATA, ArrowData.EMPTY);
        builder.add(ID_EFFECT_COLOR, -1);
        builder.add(PICKUP_STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getEntityWorld().isClient()) {
            if (this.isInGround()) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.isInGround() && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContentsComponent.DEFAULT) && this.inGroundTime >= 600) {
            this.getEntityWorld().sendEntityStatus(this, (byte)0);
            this.setStack(new ItemStack(Items.ARROW));
        }
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != -1 && particleAmount > 0) {
            for(int j = 0; j < particleAmount; ++j) {
                this.getEntityWorld().addParticleClient(TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT, i), this.getParticleX(0.5F), this.getRandomBodyY(), this.getParticleZ(0.5F), 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public int getColor() {
        return this.dataTracker.get(ID_EFFECT_COLOR);
    }

    @Override
    protected void writeCustomData(WriteView valueOutput) {
        super.writeCustomData(valueOutput);
        valueOutput.put("arrow_data", ArrowData.CODEC, this.getArrowData());
        valueOutput.putInt("color", this.getColor());
    }

    @Override
    protected void readCustomData(ReadView valueInput) {
        super.readCustomData(valueInput);
        valueInput.read("arrow_data", ArrowData.CODEC).ifPresent(arrowData -> this.dataTracker.set(ARROW_DATA, arrowData));
        this.dataTracker.set(ID_EFFECT_COLOR, valueInput.getInt("color", -1));
    }

    public void updateArrowData() {
        this.dataTracker.set(ARROW_DATA, this.dataTracker.get(PICKUP_STACK).getOrDefault(ModDataComponents.ARROW_DATA, RegistryEntry.of(ArrowData.EMPTY)).value());
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
        PotionContentsComponent potioncontents = this.getPotionContents();
        float f = this.getPotionDurationScale();
        potioncontents.forEachEffect((p_478604_) -> livingEntity.addStatusEffect(p_478604_, entity), f);
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame() || super.isOnFire();
    }

    @Override
    public void handleStatus(byte b) {
        if (b == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.getEntityWorld().addParticleClient(TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getParticleX(0.5F), this.getRandomBodyY(), this.getParticleZ(0.5F), 0.0F, 0.0F, 0.0F);
                }
            }
        } else {
            super.handleStatus(b);
        }
    }
}
