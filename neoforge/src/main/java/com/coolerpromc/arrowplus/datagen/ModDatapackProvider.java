package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.arrow.Arrows;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
            .add(ModRegistries.FEATHER_DATA_KEY, Feathers::bootstrap)
            .add(ModRegistries.STICK_DATA_KEY, Sticks::bootstrap)
            .add(ModRegistries.ARROW_DATA_KEY, Arrows::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, registrySetBuilder, Set.of(Constants.MODID));
    }
}
