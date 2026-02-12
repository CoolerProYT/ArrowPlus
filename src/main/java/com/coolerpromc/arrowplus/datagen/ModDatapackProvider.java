package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends FabricDynamicRegistryProvider{
    public ModDatapackProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
        entries.addAll(wrapperLookup.lookupOrThrow(ModRegistries.ARROW_DATA_KEY));
    }

    @Override
    public String getName() {
        return "ArrowPlus Datapack";
    }
}
