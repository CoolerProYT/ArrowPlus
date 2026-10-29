package com.coolerpromc.arrowplus.mixin.migrate;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
    private @Nullable Holder<Item> item;

    @Shadow
    private int count;

    @Mutable
    @Shadow
    @Final
    private PatchedDataComponentMap components;

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("RETURN"))
    private void migrateLegacyItem(Holder<Item> item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        MinecraftServer server = Services.PLATFORM.getServer();
        if (server == null){
            return;
        }

        ServerLevel level = server.getLevel(Level.OVERWORLD);
        if (level == null){
            return;
        }

        if (ModItems.GILDED_FEATHER.get() == item.value()){
            Holder<FeatherData> dataHolder = level.registryAccess().getOrThrow(Feathers.GILDED);
            this.item = ModItems.CUSTOM_FEATHER.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_FEATHER.get().components(), legacyComponents(components).set(ModDataComponents.FEATHER_DATA.get(), dataHolder).build());
        }

        if (ModItems.COPPER_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.COPPER);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.IRON_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.IRON);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.GOLD_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.GOLD);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.DIAMOND_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.DIAMOND);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.EMERALD_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.EMERALD);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }

        if (ModItems.NETHERITE_STICK.get() == item.value()){
            Holder<StickData> dataHolder = level.registryAccess().getOrThrow(Sticks.NETHERITE);
            this.item = ModItems.CUSTOM_STICK.get().builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(ModItems.CUSTOM_STICK.get().components(), legacyComponents(components).set(ModDataComponents.STICK_DATA.get(), dataHolder).build());
        }
    }
    private static DataComponentPatch.Builder legacyComponents(PatchedDataComponentMap components) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        components.asPatch().entrySet().forEach(entry -> entry.getValue().ifPresentOrElse(
                value -> builder.set((DataComponentType) entry.getKey(), value),
                () -> builder.remove(entry.getKey())));
        return builder;
    }
}
