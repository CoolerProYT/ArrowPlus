package com.coolerpromc.arrowplus.item.custom;

import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.PartTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ModStickItem extends Item {
    public ModStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        if (!stack.has(ModDataComponents.STICK_DATA.get())){
            ArrowParts.replaceInvalid(stack, owner, new ItemStack(Items.STICK));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltips, TooltipFlag tooltipFlag) {
        Holder<StickData> data = stack.get(ModDataComponents.STICK_DATA.get());
        if (data != null){
            tooltips.accept(Component.translatable("tooltip.arrowplus.part.stick_header").withStyle(ChatFormatting.GRAY));
            if (PartTooltips.showDetails(tooltips)){
                PartTooltips.addStick(data.value(), tooltips, false);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(stack.getOrDefault(ModDataComponents.STICK_DATA.get(), Holder.direct(StickData.EMPTY)).value().translationKey());
    }
}
