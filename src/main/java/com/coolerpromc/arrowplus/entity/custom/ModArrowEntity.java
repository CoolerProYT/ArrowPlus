package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModArrowEntity extends PersistentProjectileEntity {
    private final ItemStack stack;
    private static final TrackedData<ArrowData> ARROW_DATA = DataTracker.registerData(ModArrowEntity.class, ModEntities.ARROW_DATA);

    public ModArrowEntity(EntityType<? extends PersistentProjectileEntity> p_331098_, World p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateArrowData();
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
    }

    public ModArrowEntity(double x, double y, double z, World level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.ARROW_PLUS, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.stack = pickupItemStack;
        this.updateArrowData();
    }

    @Override
    protected @NotNull ItemStack getDefaultItemStack() {
        return stack;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ARROW_DATA, ArrowData.EMPTY);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("arrow_data", ArrowData.CODEC.encodeStart(NbtOps.INSTANCE, this.getArrowData()).getOrThrow());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(ARROW_DATA, ArrowData.CODEC.parse(NbtOps.INSTANCE, nbt.get("arrow_data")).getOrThrow());
    }

    public void updateArrowData() {
        this.dataTracker.set(ARROW_DATA, stack.getOrDefault(ModDataComponents.ARROW_DATA, ArrowData.EMPTY));
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
    protected void onHit(LivingEntity entity) {
        super.onHit(entity);
        getArrowData().effects().forEach((resourceLocation, integer) -> Registries.POTION.getEntry(resourceLocation).ifPresent(potionReference -> potionReference.value().getEffects().forEach(instance -> entity.addStatusEffect(
                new StatusEffectInstance(instance.getEffectType(), integer, instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon(), null)
        ))));
    }

    @Override
    public boolean isOnFire() {
        return getArrowData().flame();
    }
}
