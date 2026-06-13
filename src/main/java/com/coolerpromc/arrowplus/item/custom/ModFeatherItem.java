package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModFeatherItem extends Item {
    public ModFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(stack.getOrDefault(ModDataComponents.FEATHER_DATA, Holder.direct(FeatherData.EMPTY)).value().translationKey());
    }
}
