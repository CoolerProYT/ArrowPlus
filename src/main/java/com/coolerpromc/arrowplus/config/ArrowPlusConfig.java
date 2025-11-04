package com.coolerpromc.arrowplus.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ArrowPlusConfig {
    public static final ArrowPlusConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> removal;

    static {
        Pair<ArrowPlusConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ArrowPlusConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    private ArrowPlusConfig(ForgeConfigSpec.Builder builder){
        builder.push("Restrictions");
        removal = builder.comment("A list of arrow to be disabled. Example: ['diamond', 'iron']").defineList("restrictions", List.of(), o -> o instanceof String);
        builder.pop();
    }

    public List<? extends String> getRemoval() {
        return removal.get();
    }
}
