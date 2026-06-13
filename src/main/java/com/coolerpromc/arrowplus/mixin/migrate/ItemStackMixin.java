package com.coolerpromc.arrowplus.mixin.migrate;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated(forRemoval = true)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Mutable
    @Shadow
    @Final
    @Deprecated
    private @Nullable Item item;

    @Shadow
    private int count;

    @Mutable
    @Shadow
    @Final
    PatchedDataComponentMap components;

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("RETURN"))
    private void migrateLegacyItem(ItemLike item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        if (!(ModItems.GILDED_FEATHER.isBound() || ModItems.COPPER_STICK.isBound() || ModItems.IRON_STICK.isBound() || ModItems.GOLD_STICK.isBound() || ModItems.DIAMOND_STICK.isBound() || ModItems.EMERALD_STICK.isBound() || ModItems.NETHERITE_STICK.isBound())) return;;
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        var level = server.getLevel(Level.OVERWORLD);
        if (level == null) return;

        if (ModItems.GILDED_FEATHER.get() == item) {
            Holder<FeatherData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Feathers.GILDED);
            this.item = ModItems.CUSTOM_FEATHER.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_FEATHER.get().components(), DataComponentPatch.builder().set(ModDataComponents.FEATHER_DATA.get(), dataHolder).build());
        }

        if (ModItems.COPPER_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.COPPER);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.IRON_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.IRON);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.GOLD_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.GOLD);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.DIAMOND_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.DIAMOND);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.EMERALD_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.EMERALD);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.NETHERITE_STICK.get() == item) {
            Holder<StickData> dataHolder = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).registryAccess().holderOrThrow(Sticks.NETHERITE);
            this.item = ModItems.CUSTOM_STICK.get();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), DataComponentPatch.builder().set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }
    }
}
