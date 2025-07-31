package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArrowPlus.MODID);

    public static final RegistryObject<CreativeModeTab> ARROW_PLUS_TAB = CREATIVE_MOD_TABS.register("arrow_plus",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ARROW))
                    .title(Component.translatable("creativetab.arrowplus"))
                    .displayItems((itemDisplayParameters, output) -> {
                        itemDisplayParameters.holders().lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements().map(Holder.Reference::value).forEach(arrowData -> {
                            ItemStack arrow = ModItems.ARROW_PLUS.get().getDefaultInstance();
                            arrow.set(ModDataComponents.ARROW_DATA.get(), arrowData);
                            output.accept(arrow);
                        });
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
