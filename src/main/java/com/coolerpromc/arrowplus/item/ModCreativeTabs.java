package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCreativeTabs {
    public static final ItemGroup ARROW_PLUS_TAB = Registry.register(Registries.ITEM_GROUP, Identifier.of(ArrowPlus.MODID, "arrow_plus"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.ARROW))
                    .displayName(Text.translatable("creativetab.arrowplus"))
                    .entries((itemDisplayParameters, output) -> {
                        itemDisplayParameters.lookup().getOptional(ModRegistries.ARROW_DATA_KEY).ifPresent(impl ->
                                impl.streamEntries().map(RegistryEntry.Reference::value).forEach(arrowData -> {
                                    ItemStack arrow = ModItems.ARROW_PLUS.getDefaultStack();
                                    arrow.set(ModDataComponents.ARROW_DATA, arrowData);
                                    output.add(arrow);
                                })
                        );
                    })
                    .build()
    );

    public static void register() {

    }
}
