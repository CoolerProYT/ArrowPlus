package com.coolerpromc.arrowplus.item.custom;

import net.minecraft.item.Item;

public class ModStickItem extends Item {
    private final int color;

    public ModStickItem(Settings properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}