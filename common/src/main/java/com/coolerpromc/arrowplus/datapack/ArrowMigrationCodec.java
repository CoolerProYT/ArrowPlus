package com.coolerpromc.arrowplus.datapack;

import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class ArrowMigrationCodec<V> extends MapCodec<V> {
    private final MapCodec<V> delegate;
    private final BiFunction<DynamicOps<?>, V, V> migration;

    private ArrowMigrationCodec(MapCodec<V> delegate, BiFunction<DynamicOps<?>, V, V> migration) {
        this.delegate = delegate;
        this.migration = migration;
    }

    public static MapCodec<ItemStack> wrapStack(MapCodec<ItemStack> delegate) {
        return new ArrowMigrationCodec<>(delegate, ArrowMigrationCodec::migrateStack);
    }

    public static MapCodec<ItemStackTemplate> wrapTemplate(MapCodec<ItemStackTemplate> delegate) {
        return new ArrowMigrationCodec<>(delegate, ArrowMigrationCodec::migrateTemplate);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return delegate.keys(ops);
    }

    @Override
    public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
        return this.delegate.decode(ops, input).map(value -> this.migration.apply(ops, value));
    }

    @Override
    public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return delegate.encode(input, ops, prefix);
    }

    private static ItemStack migrateStack(DynamicOps<?> ops, ItemStack stack) {
        if (stack.isEmpty() || !(ops instanceof RegistryOps<?> registryOps)){
            return stack;
        }
        Optional<HolderGetter<StickData>> sticks = registryOps.getter(ModRegistries.STICK_DATA_KEY);
        Optional<HolderGetter<FeatherData>> feathers = registryOps.getter(ModRegistries.FEATHER_DATA_KEY);
        if (sticks.isPresent() && feathers.isPresent()){
            ArrowParts.migrate(stack, sticks.get(), feathers.get());
        }
        return stack;
    }

    private static ItemStackTemplate migrateTemplate(DynamicOps<?> ops, ItemStackTemplate template) {
        if (!(ops instanceof RegistryOps<?> registryOps)){
            return template;
        }
        Optional<HolderGetter<StickData>> sticks = registryOps.getter(ModRegistries.STICK_DATA_KEY);
        Optional<HolderGetter<FeatherData>> feathers = registryOps.getter(ModRegistries.FEATHER_DATA_KEY);
        if (sticks.isPresent() && feathers.isPresent()){
            return ArrowParts.migrate(template, sticks.get(), feathers.get());
        }
        return template;
    }

    @Override
    public String toString() {
        return "ArrowMigration[" + delegate + "]";
    }
}
