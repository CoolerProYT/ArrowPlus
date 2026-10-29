package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NullableProblems")
public class ModArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<ArrowData> ARROW_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.ARROW_DATA.get());
    private static final EntityDataAccessor<StickData> STICK_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.STICK_DATA.get());
    private static final EntityDataAccessor<FeatherData> FEATHER_DATA = SynchedEntityData.defineId(ModArrowEntity.class, ModEntities.FEATHER_DATA.get());
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR  = SynchedEntityData.defineId(ModArrowEntity.class, EntityDataSerializers.INT);

    public ModArrowEntity(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.setPickupItemStack(pickupItemStack.copyWithCount(1));
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon, double baseDamage) {
        super(entityType, owner, level, pickupItemStack.copyWithCount(1), firedFromWeapon);
        this.pickup = Pickup.ALLOWED;

        Holder<ArrowData> data = pickupItemStack.get(ModDataComponents.ARROW_DATA.get());

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof ProjectileWeaponItem && !pickupItemStack.has(DataComponents.POTION_CONTENTS)){
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.INFINITY), firedFromWeapon);
            if (data != null){
                this.pickup = infinityLevel > 0&& !ArrowPlusConfig.isInfinityBlacklisted(data.unwrapKey().get().identifier().getPath()) ? Pickup.DISALLOWED : Pickup.ALLOWED;
            }
            else{
                this.pickup = infinityLevel > 0 ? Pickup.DISALLOWED : Pickup.ALLOWED;
            }
        }
        Unit unit = pickupItemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null){
            this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        this.setBaseDamage(baseDamage);
        this.setPickupItemStack(pickupItemStack.copyWithCount(1));
    }

    public ModArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setPickupItemStack(pickupItemStack.copyWithCount(1));
    }

    private PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private float getPotionDurationScale() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F);
    }

    @Override
    protected void setPickupItemStack(ItemStack stack) {
        super.setPickupItemStack(stack);
        this.updateArrowData(this.getPickupItemStackOrigin());
        this.updateColor();
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.ARROW_PLUS.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ARROW_DATA, ArrowData.EMPTY);
        builder.define(STICK_DATA, StickData.VANILLA);
        builder.define(FEATHER_DATA, FeatherData.VANILLA);
        builder.define(ID_EFFECT_COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()){
            if (this.isInGround()){
                if (this.inGroundTime % 5 == 0){
                    this.makeParticle(1);
                }
            }
            else{
                this.makeParticle(2);
            }
        }
        else if (this.isInGround() && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600){
            this.level().broadcastEntityEvent(this, (byte)0);
            ItemStack untipped = this.getPickupItemStackOrigin().copy();
            untipped.remove(DataComponents.POTION_CONTENTS);
            this.setPickupItemStack(untipped);
        }
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != -1 && particleAmount > 0){
            for (int j = 0; j < particleAmount; ++j){
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
        valueOutput.store("stick_data", StickData.CODEC, this.getStickData());
        valueOutput.store("feather_data", FeatherData.CODEC, this.getFeatherData());
        valueOutput.putInt("color", this.getColor());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        valueInput.read("arrow_data", ArrowData.CODEC).ifPresent(arrowData -> this.entityData.set(ARROW_DATA, arrowData));
        valueInput.read("stick_data", StickData.CODEC).ifPresent(stickData -> this.entityData.set(STICK_DATA, stickData));
        valueInput.read("feather_data", FeatherData.CODEC).ifPresent(featherData -> this.entityData.set(FEATHER_DATA, featherData));
        this.entityData.set(ID_EFFECT_COLOR, valueInput.getIntOr("color", -1));
    }

    public void updateArrowData(ItemStack stack){
        this.entityData.set(ARROW_DATA, ArrowParts.head(stack));
        this.entityData.set(STICK_DATA, ArrowParts.stick(stack));
        this.entityData.set(FEATHER_DATA, ArrowParts.feather(stack));
    }

    public ArrowData getArrowData(){
        return this.entityData.get(ARROW_DATA);
    }

    public StickData getStickData(){
        return this.entityData.get(STICK_DATA);
    }

    public FeatherData getFeatherData(){
        return this.entityData.get(FEATHER_DATA);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity * (float) getStickData().velocity(), inaccuracy * (float) getFeatherData().accuracy());
    }

    @Override
    protected float getWaterInertia() {
        return (float) getFeatherData().waterDrag();
    }

    @Override
    public Component getName() {
        return Component.translatable(getArrowData().translationKey());
    }

    @Override
    protected double getDefaultGravity() {
        return getStickData().gravity() * getFeatherData().gravityMultiplier();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity) {
        super.doPostHurtEffects(livingEntity);
        getArrowData().effectInstances().forEach(livingEntity::addEffect);
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
        if (b == 0){
            int i = this.getColor();
            if (i != -1){
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;

                for (int j = 0; j < 20; ++j){
                    this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getRandomX(0.5F), this.getRandomY(), this.getRandomZ(0.5F), 0.0F, 0.0F, 0.0F);
                }
            }
        }
        else{
            super.handleEntityEvent(b);
        }
    }
}
