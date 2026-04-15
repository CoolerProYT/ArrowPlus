
package com.coolerpromc.arrowplus.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeArrowPlusConfig {
    public static final NeoForgeArrowPlusConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<List<? extends String>> removal;

    static {
        Pair<NeoForgeArrowPlusConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(NeoForgeArrowPlusConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    private NeoForgeArrowPlusConfig(ModConfigSpec.Builder builder){
        builder.push("Restrictions");
        removal = builder.comment("A list of arrow to be disabled. Example: ['diamond', 'iron']").defineList("restrictions", List.of(), o -> o instanceof String);
        builder.pop();
    }

    public List<? extends String> getRemoval() {
        return removal.get();
    }

    public static void onConfigLoad(ModConfigEvent.Loading event) {
        syncNeoForgeConfig();
    }

    public static void onConfigReload(ModConfigEvent.Reloading event) {
        syncNeoForgeConfig();
    }

    private static void syncNeoForgeConfig() {
        ArrowPlusConfig.sync(new ArrayList<>(NeoForgeArrowPlusConfig.CONFIG.getRemoval()));
    }
}