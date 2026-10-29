package com.coolerpromc.arrowplus.datapack.feather;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Feathers {
    public static final ResourceKey<FeatherData> FEATHER = key("feather");
    public static final ResourceKey<FeatherData> PHANTOM_MEMBRANE = key("phantom_membrane");
    public static final ResourceKey<FeatherData> PAPER = key("paper");
    public static final ResourceKey<FeatherData> GILDED = key("gilded");
    public static final ResourceKey<FeatherData> KELP = key("kelp");
    public static final ResourceKey<FeatherData> PRISMARINE = key("prismarine");

    public static final TagKey<Item> FEATHERS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "feathers"));

    public static void bootstrap(BootstrapContext<FeatherData> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        context.register(FEATHER, FeatherData.standalone(items.getOrThrow(FEATHERS), -1, "item.minecraft.feather", 0.99, 0.6, 1.0));
        context.register(PHANTOM_MEMBRANE, new FeatherData(HolderSet.direct(Items.PHANTOM_MEMBRANE.builtInRegistryHolder()), 0xFFC9C0E0, "item.minecraft.phantom_membrane", 1, 0.995, 0.6, 0.8, true, 0.6));
        context.register(PAPER, FeatherData.standalone(Items.PAPER, 0xFFF2F2F2, "item.minecraft.paper", 0.98, 0.5, 1.3));

        context.register(GILDED, new FeatherData(Items.GLOWSTONE_DUST, 0xFFfffc69, "item.arrowplus.gilded_feather", 4, 0.99, 0.6, 0.8));
        context.register(KELP, new FeatherData(Items.DRIED_KELP, 0xFF4B6B2A, "item.arrowplus.kelp_feather", 4, 0.985, 0.95, 1.1));
        context.register(PRISMARINE, new FeatherData(Items.PRISMARINE_SHARD, 0xFF5EC8C8, "item.arrowplus.prismarine_feather", 4, 0.99, 0.85, 0.9));
    }

    private static ResourceKey<FeatherData> key(String name){
        return ResourceKey.create(ModRegistries.FEATHER_DATA_KEY, Constants.id(name));
    }
}
