package com.coolerpromc.arrowplus.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

public class Codecs {
    public static final Codec<HolderSet<Item>> ITEM_HOLDER_SET_CODEC = RegistryCodecs.homogeneousList(Registries.ITEM);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<Item>> ITEM_HOLDER_SET_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM);
}
