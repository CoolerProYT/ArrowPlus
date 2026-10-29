package com.coolerpromc.arrowplus.compat.jei;

import com.coolerpromc.arrowplus.Constants;
import net.minecraft.resources.Identifier;

public final class JeiRecipeIds {
    private JeiRecipeIds() {
    }

    public static Identifier arrow(Identifier head) {
        return Constants.id("jei/arrow/" + head.getNamespace() + "/" + head.getPath());
    }

    public static Identifier tipped(Identifier head, Identifier potion) {
        return Constants.id("jei/tipped/" + head.getNamespace() + "/" + head.getPath().length() + "_"
                + head.getPath() + "/" + potion.getNamespace() + "/" + potion.getPath());
    }
}
