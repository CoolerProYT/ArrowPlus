
package com.coolerpromc.arrowplus.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ArrowPlusConfig {
    public static final ArrowPlusConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<List<? extends String>> removal;
    public final ModConfigSpec.ConfigValue<List<? extends String>> infinityBlacklist;

    static {
        Pair<ArrowPlusConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ArrowPlusConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    private ArrowPlusConfig(ModConfigSpec.Builder builder){
        builder.push("Restrictions");
        removal = builder.comment("A list of arrow to be disabled. Example: ['diamond', 'iron']").defineList("restrictions", List.of(), o -> o instanceof String);
        infinityBlacklist = builder.comment("A list of arrow that won't be affected by infinity enchantment. Example: ['diamond', 'iron']").defineList("infinityBlacklist", List.of(), o -> o instanceof String);
        builder.pop();
    }

    public List<? extends String> getRemoval() {
        return removal.get();
    }

    public boolean isInfinityBlacklisted(String name){
        return infinityBlacklist.get().contains(name);
    }
}