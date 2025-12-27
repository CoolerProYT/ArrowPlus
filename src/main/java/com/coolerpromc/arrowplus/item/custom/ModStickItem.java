package com.coolerpromc.arrowplus.item.custom;

import net.minecraft.world.item.Item;

public class ModStickItem extends Item {
    private final int color;

    public ModStickItem(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
