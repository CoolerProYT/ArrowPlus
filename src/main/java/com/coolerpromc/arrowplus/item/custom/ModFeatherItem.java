package com.coolerpromc.arrowplus.item.custom;

import net.minecraft.item.Item;

public class ModFeatherItem extends Item {
    private final int color;

    public ModFeatherItem(Settings properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}