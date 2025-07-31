package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends VanillaItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockLookupProvider) {
        super(output, lookupProvider, blockLookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.ARROWS)
                .add(ModItems.ARROW_PLUS.get());
    }
}
