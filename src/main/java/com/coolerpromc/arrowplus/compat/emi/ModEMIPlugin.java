package com.coolerpromc.arrowplus.compat.emi;

import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.item.ModItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

@EmiEntrypoint
public class ModEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        if (!ArrowPlusConfig.CONFIG.hideTippedArrow()) return;
        var access = Minecraft.getInstance().level.registryAccess();

        registry.removeEmiStacks(s -> {
            ItemStack stack = s.getItemStack();
            return !stack.isEmpty()
                    && stack.getItem() == ModItems.ARROW_PLUS.get()
                    && stack.has(DataComponents.POTION_CONTENTS);
        });
    }
}
