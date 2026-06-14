package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class DatapackProvider extends FabricDynamicRegistryProvider {
    public DatapackProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY));
        entries.addAll(registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY));
        entries.addAll(registries.lookupOrThrow(ModRegistries.ARROW_DATA_KEY));
    }

    @Override
    public String getName() {
        return "";
    }
}
