package com.coolerpromc.arrowplus.datapack;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class LegacyArrowParts {
    private LegacyArrowParts() {
    }

    public static ResourceKey<StickData> stick(Identifier head) {
        if (!head.getNamespace().equals(Constants.MODID)){
            return Sticks.STICK;
        }
        return switch (head.getPath()){
            case "iron" -> Sticks.COPPER;
            case "gold" -> Sticks.IRON;
            case "diamond" -> Sticks.GOLD;
            case "emerald", "obsidian", "netherite" -> Sticks.DIAMOND;
            default -> Sticks.STICK;
        };
    }

    public static ResourceKey<FeatherData> feather(Identifier head) {
        if (head.getNamespace().equals(Constants.MODID) && (head.getPath().equals("obsidian") || head.getPath().equals("netherite"))){
            return Feathers.GILDED;
        }
        return Feathers.FEATHER;
    }
}
