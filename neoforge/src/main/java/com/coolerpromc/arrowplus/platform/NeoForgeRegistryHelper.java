package com.coolerpromc.arrowplus.platform;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.platform.services.IRegistryHelper;
import com.coolerpromc.arrowplus.platform.util.RegistryHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MODID);
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(Constants.MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Constants.MODID);
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Constants.MODID);

    @Override
    public <T extends Item> RegistryHandler<T> registerItem(String name, Function<Item.Properties, T> func) {
        DeferredItem<T> item = ITEMS.registerItem(name, func);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return item.getId();
            }

            @Override
            public Holder<T> holder() {
                return (Holder<T>) item.getDelegate();
            }

            @Override
            public T get() {
                return item.get();
            }
        };
    }

    @Override
    public RegistryHandler<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Component title, Function<CreativeModeTab.ItemDisplayParameters, ItemStack[]> func) {
        DeferredHolder<CreativeModeTab, CreativeModeTab> tab = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder().icon(icon).title(title).displayItems(((param, output) -> Arrays.stream(func.apply(param)).forEach(output::accept))).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return tab.getId();
            }

            @Override
            public Holder<CreativeModeTab> holder() {
                return tab.getDelegate();
            }

            @Override
            public CreativeModeTab get() {
                return tab.value();
            }
        };
    }

    @Override
    public <T> RegistryHandler<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DeferredHolder<DataComponentType<?>, DataComponentType<T>> component = COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return component.getId();
            }

            @Override
            public Holder<DataComponentType<T>> holder() {
                return (Holder<DataComponentType<T>>) (Holder<?>) component.getDelegate();
            }

            @Override
            public DataComponentType<T> get() {
                return component.get();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer) {
        DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> holder = RECIPE_SERIALIZERS.register(name, () -> serializer);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return holder.getId();
            }

            @Override
            public Holder<RecipeSerializer<T>> holder() {
                return (Holder<RecipeSerializer<T>>) (Holder<?>) holder.getDelegate();
            }

            @Override
            public RecipeSerializer<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends Entity> RegistryHandler<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> builder) {
        DeferredHolder<EntityType<?>, EntityType<T>> holder = ENTITIES.registerEntityType(name, factory, category, builder);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return holder.getId();
            }

            @Override
            public Holder<EntityType<T>> holder() {
                return (Holder<EntityType<T>>) (Holder<?>) holder.getDelegate();
            }

            @Override
            public EntityType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T> RegistryHandler<EntityDataSerializer<T>> registerEntityDataSerializer(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<T>> holder = ENTITY_SERIALIZERS.register(name, () -> EntityDataSerializer.forValueType(streamCodec));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return holder.getId();
            }

            @Override
            public Holder<EntityDataSerializer<T>> holder() {
                return (Holder<EntityDataSerializer<T>>) (Holder<?>) holder;
            }

            @Override
            public EntityDataSerializer<T> get() {
                return holder.value();
            }
        };
    }

    public static void register(IEventBus eventBus){
        ENTITIES.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        COMPONENTS.register(eventBus);
        ENTITY_SERIALIZERS.register(eventBus);
    }
}
