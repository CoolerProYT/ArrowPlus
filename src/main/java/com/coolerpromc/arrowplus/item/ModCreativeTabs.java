package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.util.ArrowData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArrowPlus.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARROW_PLUS_TAB = CREATIVE_MOD_TABS.register("arrow_plus",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ARROW))
                    .title(Component.translatable("creativetab.arrowplus"))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (ItemStack entry : ArrowData.VARIANT_STACKS) {
                            output.accept(entry);
                        }
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
