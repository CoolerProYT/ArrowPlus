package com.coolerpromc.arrowplus.entity.custom;

import com.coolerpromc.arrowplus.entity.ModEntities;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class ModArrowEntity extends PersistentProjectileEntity {
    private final ItemStack stack;
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(ModArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public ModArrowEntity(EntityType<? extends PersistentProjectileEntity> p_331098_, World p_331626_, ItemStack pickupItemStack) {
        super(p_331098_, p_331626_);
        this.stack = pickupItemStack;
        this.updateColor();
    }

    public ModArrowEntity(LivingEntity owner, World level, ItemStack pickupItemStack, double baseDamage) {
        super(ModEntities.ARROW_PLUS, owner, level);
        this.stack = pickupItemStack;
        this.pickupType = PickupPermission.ALLOWED;
        ItemStack firedFromWeapon = owner.getActiveItem();

        if (firedFromWeapon != null && firedFromWeapon.getItem() instanceof BowItem){
            int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, firedFromWeapon);

            if (powerLevel > 0) {
                baseDamage += (baseDamage * 0.25D) * (powerLevel + 1);
            }

            int infinityLevel = EnchantmentHelper.getLevel(Enchantments.INFINITY, firedFromWeapon);
            this.pickupType = infinityLevel > 0 ? PickupPermission.DISALLOWED : PickupPermission.ALLOWED;
        }
        this.setDamage(baseDamage);
        this.updateColor();
    }

    public ModArrowEntity(double x, double y, double z, World level, ItemStack pickupItemStack) {
        super(ModEntities.ARROW_PLUS, x, y, z, level);
        this.stack = pickupItemStack;
        this.updateColor();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COLOR, -1);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("color", this.getColor());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(COLOR, nbt.getInt("color"));
    }

    @Override
    protected ItemStack asItemStack() {
        return stack;
    }

    public void updateColor() {
        this.dataTracker.set(COLOR, ArrowData.load(stack.getOrCreateNbt()).color());
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }
}
