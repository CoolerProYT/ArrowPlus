
package com.coolerpromc.arrowplus.config;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.coolerconfig.config.ConfigBuilder;
import com.coolerpromc.coolerconfig.config.ConfigFormat;
import com.coolerpromc.coolerconfig.config.ConfigSpec;
import com.coolerpromc.coolerconfig.config.ConfigValue;

import java.util.List;

public class ArrowPlusConfig {
    public static ConfigValue<List<String>> removal;
    public static ConfigValue<List<String>> infinityBlacklist;

    public static ConfigSpec CONFIG;

    public static void init(){
        ConfigBuilder builder = ConfigSpec.builder(Constants.MODID, ConfigFormat.TOML).watchForChanges();

        removal = builder.defineList("Restrictions.restrictions", List.of(), "A list of arrow to be disabled. Example: ['diamond', 'iron']");
        infinityBlacklist = builder.defineList("Restrictions.infinityBlacklist", List.of(), "A list of arrow that won't be affected by infinity enchantment. Example: ['diamond', 'iron']");

        CONFIG = builder.build();
    }

    public static List<String> getRemoval() {
        return removal.get();
    }

    public static boolean isInfinityBlacklisted(String name){
        return infinityBlacklist.get().contains(name);
    }
}