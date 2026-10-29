package com.coolerpromc.arrowplus.platform;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.platform.services.IRegistryHelper;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func) {
        Identifier id = Constants.id(name);
        ResourceKey<Item> key = IRegistryHelper.itemKey(name);
        Holder<T> item = Registry.registerForHolder(BuiltInRegistries.ITEM, id, func.apply(new Item.Properties().setId(key)));

        return new RegistryHandler.Items<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<T> holder() {
                return item;
            }

            @Override
            public T get() {
                return item.value();
            }
        };
    }

    @Override
    public RegistryHandler<CreativeModeTab, CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Component title, Function<CreativeModeTab.ItemDisplayParameters, ItemStack[]> func) {
        Identifier id = Constants.id(name);
        Holder<CreativeModeTab> holder = Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, id, FabricCreativeModeTab.builder().icon(icon).title(title).displayItems((parameters, output) -> Arrays.stream(func.apply(parameters)).forEach(output::accept)).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<CreativeModeTab> holder() {
                return holder;
            }

            @Override
            public CreativeModeTab get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T> RegistryHandler<DataComponentType<?>, DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        Identifier id = Constants.id(name);
        Holder<DataComponentType<T>> holder = Registry.registerForHolder(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builder.apply(DataComponentType.builder()).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<DataComponentType<T>> holder() {
                return holder;
            }

            @Override
            public DataComponentType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeSerializer<?>, RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer) {
        Identifier id = Constants.id(name);
        Holder<RecipeSerializer<T>> holder = Registry.registerForHolder(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<RecipeSerializer<T>> holder() {
                return holder;
            }

            @Override
            public RecipeSerializer<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends Entity> RegistryHandler<EntityType<?>, EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> builder) {
        Identifier id = Constants.id(name);
        ResourceKey<EntityType<?>> key = IRegistryHelper.entityKey(name);
        Holder<EntityType<T>> holder = Registry.registerForHolder(BuiltInRegistries.ENTITY_TYPE, id, builder.apply(EntityType.Builder.of(factory, category)).build(key));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<EntityType<T>> holder() {
                return holder;
            }

            @Override
            public EntityType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T> RegistryHandler<EntityDataSerializer<?>, EntityDataSerializer<T>> registerEntityDataSerializer(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        Identifier id = Constants.id(name);
        EntityDataSerializer<T> serializer = EntityDataSerializer.forValueType(streamCodec);
        FabricEntityDataRegistry.register(id, serializer);

        return new RegistryHandler<>() {
            @Override
            public EntityDataSerializer<T> get() {
                return serializer;
            }

            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<EntityDataSerializer<T>> holder() {
                return Holder.direct(serializer);
            }
        };
    }
}
