package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.datapack.arrow.Arrows;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.RegistrySetBuilder;

public class ModDatapackProvider {
    public static final RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
            .add(ModRegistries.FEATHER_DATA_KEY, Feathers::bootstrap)
            .add(ModRegistries.STICK_DATA_KEY, Sticks::bootstrap)
            .add(ModRegistries.ARROW_DATA_KEY, Arrows::bootstrap);
}
