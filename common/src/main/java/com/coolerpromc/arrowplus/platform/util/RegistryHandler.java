package com.coolerpromc.arrowplus.platform.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface RegistryHandler<R, T extends R> extends Supplier<T> {
    Identifier id();
    Holder<T> holder();

    default ResourceKey<R> key(){
        return (ResourceKey<R>) holder().unwrapKey().orElse(null);
    }

    default ItemStack toStack() {
        if (get() instanceof ItemLike item){
            return item.asItem().getDefaultInstance();
        }
        throw new UnsupportedOperationException("Cannot create ItemStack from " + get());
    }

    interface Items<I extends Item> extends RegistryHandler<Item, I>, ItemLike{
        @Override
        default Item asItem(){
            return get();
        }

        default ItemStack toStack() {
            return asItem().getDefaultInstance();
        }
    }
}