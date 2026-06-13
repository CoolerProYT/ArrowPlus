package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModStickItem extends Item {
    public ModStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(stack.getOrDefault(ModDataComponents.STICK_DATA, Holder.direct(StickData.EMPTY)).value().translationKey());
    }
}
