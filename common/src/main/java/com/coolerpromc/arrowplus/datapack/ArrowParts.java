package com.coolerpromc.arrowplus.datapack;

import com.coolerpromc.arrowplus.Constants;
import com.coolerpromc.arrowplus.config.ArrowPlusConfig;
import com.coolerpromc.arrowplus.datacomponent.ModDataComponents;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.feather.Feathers;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.datapack.stick.Sticks;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.coolerpromc.arrowplus.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("removal")
public final class ArrowParts {
    private ArrowParts() {
    }

    public static Optional<Holder.Reference<ArrowData>> findHead(ItemStack material, HolderLookup.Provider registries) {
        if (material.isEmpty()){
            return Optional.empty();
        }
        return registries.lookupOrThrow(ModRegistries.ARROW_DATA_KEY).listElements()
                .filter(reference -> !ArrowPlusConfig.getRemoval().contains(reference.key().identifier().getPath()))
                .filter(reference -> reference.value().ingredient().test(material))
                .findFirst();
    }

    public static Optional<Holder<StickData>> findStick(ItemStack stack, HolderLookup.Provider registries) {
        if (stack.isEmpty()){
            return Optional.empty();
        }
        if (stack.getItem() instanceof ModStickItem){
            return Optional.ofNullable(stack.get(ModDataComponents.STICK_DATA.get()));
        }
        return findStandalone(stack, registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY), StickData::standalone, StickData::ingredient);
    }

    public static Optional<Holder<FeatherData>> findFeather(ItemStack stack, HolderLookup.Provider registries) {
        if (stack.isEmpty()){
            return Optional.empty();
        }
        if (stack.getItem() instanceof ModFeatherItem){
            return Optional.ofNullable(stack.get(ModDataComponents.FEATHER_DATA.get()));
        }
        return findStandalone(stack, registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY), FeatherData::standalone, FeatherData::ingredient);
    }

    private static <T> Optional<Holder<T>> findStandalone(ItemStack stack, HolderGetter<T> parts, Predicate<T> standalone, Function<T, Ingredient> ingredient) {
        if (!(parts instanceof HolderLookup<T> lookup)){
            return Optional.empty();
        }
        return lookup.listElements()
                .filter(Holder::isBound)
                .filter(reference -> standalone.test(reference.value()) && ingredient.apply(reference.value()).test(stack))
                .<Holder<T>>map(reference -> reference)
                .findFirst();
    }

    public static List<StickData> craftedSticks(ItemStack material, HolderLookup.Provider registries) {
        return findCrafted(material, registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY), StickData::standalone, StickData::ingredient);
    }

    public static List<FeatherData> craftedFeathers(ItemStack material, HolderLookup.Provider registries) {
        return findCrafted(material, registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY), FeatherData::standalone, FeatherData::ingredient);
    }

    private static <T> List<T> findCrafted(ItemStack stack, HolderLookup<T> parts, Predicate<T> standalone, Function<T, Ingredient> ingredient) {
        if (stack.isEmpty()){
            return List.of();
        }
        return parts.listElements()
                .map(Holder.Reference::value)
                .filter(part -> !standalone.test(part) && ingredient.apply(part).test(stack))
                .toList();
    }

    public static void replaceInvalid(ItemStack stack, Entity owner, ItemStack replacement) {
        if (!(owner instanceof Player player)){
            return;
        }
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++){
            if (inventory.getItem(slot) == stack){
                inventory.setItem(slot, replacement.copyWithCount(stack.getCount()));
                return;
            }
        }
    }

    public static Optional<Holder<StickData>> defaultStick(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY).get(Sticks.STICK).map(reference -> reference);
    }

    public static Optional<Holder<FeatherData>> defaultFeather(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY).get(Feathers.FEATHER).map(reference -> reference);
    }

    public static ItemStack create(Holder<ArrowData> head, Optional<Holder<StickData>> stick, Optional<Holder<FeatherData>> feather, int count) {
        ItemStack arrow = new ItemStack(ModItems.ARROW_PLUS.get(), count);
        arrow.set(ModDataComponents.ARROW_DATA.get(), head);
        stick.ifPresent(holder -> arrow.set(ModDataComponents.STICK_DATA.get(), holder));
        feather.ifPresent(holder -> arrow.set(ModDataComponents.FEATHER_DATA.get(), holder));
        return arrow;
    }

    public static ItemStack createDefault(Holder<ArrowData> head, HolderLookup.Provider registries, int count) {
        return create(head, defaultStick(registries), defaultFeather(registries), count);
    }

    public static ArrowData head(ItemStack arrow) {
        return arrow.getOrDefault(ModDataComponents.ARROW_DATA.get(), Holder.direct(ArrowData.EMPTY)).value();
    }

    public static StickData stick(ItemStack arrow) {
        Holder<StickData> stick = arrow.get(ModDataComponents.STICK_DATA.get());
        if (stick != null){
            return stick.value();
        }
        return head(arrow).stickData().map(Holder::value).orElse(StickData.VANILLA);
    }

    public static FeatherData feather(ItemStack arrow) {
        Holder<FeatherData> feather = arrow.get(ModDataComponents.FEATHER_DATA.get());
        if (feather != null){
            return feather.value();
        }
        return head(arrow).featherData().map(Holder::value).orElse(FeatherData.VANILLA);
    }

    public static boolean migrate(ItemStack arrow, HolderLookup.Provider registries) {
        return migrate(arrow, registries.lookupOrThrow(ModRegistries.STICK_DATA_KEY), registries.lookupOrThrow(ModRegistries.FEATHER_DATA_KEY));
    }

    public static boolean migrate(ItemStack arrow, HolderGetter<StickData> sticks, HolderGetter<FeatherData> feathers) {
        if (!arrow.is(ModItems.ARROW_PLUS.get())){
            return false;
        }
        Holder<ArrowData> head = arrow.get(ModDataComponents.ARROW_DATA.get());
        if (head == null || !head.isBound()){
            return false;
        }
        boolean changed = false;
        if (!arrow.has(ModDataComponents.STICK_DATA.get())){
            Optional<Holder<StickData>> stick = legacyStick(head, sticks);
            if (stick.isPresent()){
                arrow.set(ModDataComponents.STICK_DATA.get(), stick.get());
                changed = true;
            }
        }
        if (!arrow.has(ModDataComponents.FEATHER_DATA.get())){
            Optional<Holder<FeatherData>> feather = legacyFeather(head, feathers);
            if (feather.isPresent()){
                arrow.set(ModDataComponents.FEATHER_DATA.get(), feather.get());
                changed = true;
            }
        }
        return changed;
    }

    public static ItemStackTemplate migrate(ItemStackTemplate template, HolderGetter<StickData> sticks, HolderGetter<FeatherData> feathers) {
        if (!template.item().is(Constants.id("arrow_plus")) || !template.item().areComponentsBound()){
            return template;
        }
        DataComponentPatch.SplitResult patch = template.components().split();
        Holder<ArrowData> head = patch.added().get(ModDataComponents.ARROW_DATA.get());
        if (head == null || !head.isBound()){
            return template;
        }
        Optional<Holder<StickData>> stick = patch.added().get(ModDataComponents.STICK_DATA.get()) == null ? legacyStick(head, sticks) : Optional.empty();
        Optional<Holder<FeatherData>> feather = patch.added().get(ModDataComponents.FEATHER_DATA.get()) == null ? legacyFeather(head, feathers) : Optional.empty();
        if (stick.isEmpty() && feather.isEmpty()){
            return template;
        }
        DataComponentPatch.Builder components = DataComponentPatch.builder().set(patch.added());
        for (DataComponentType<?> removed : patch.removed()){
            components.remove(removed);
        }
        stick.ifPresent(holder -> components.set(ModDataComponents.STICK_DATA.get(), holder));
        feather.ifPresent(holder -> components.set(ModDataComponents.FEATHER_DATA.get(), holder));
        return new ItemStackTemplate(template.item(), template.count(), components.build());
    }

    private static Optional<Holder<StickData>> legacyStick(Holder<ArrowData> head, HolderGetter<StickData> sticks) {
        return head.value().stickData()
                .or(() -> head.value().stick().flatMap(item -> findStandalone(new ItemStack(item), sticks, StickData::standalone, StickData::ingredient)))
                .or(() -> sticks.get(head.unwrapKey().map(key -> LegacyArrowParts.stick(key.identifier())).orElse(Sticks.STICK)).map(reference -> reference))
                .or(() -> sticks.get(Sticks.STICK).map(reference -> reference));
    }

    private static Optional<Holder<FeatherData>> legacyFeather(Holder<ArrowData> head, HolderGetter<FeatherData> feathers) {
        return head.value().featherData()
                .or(() -> head.value().feather().flatMap(item -> findStandalone(new ItemStack(item), feathers, FeatherData::standalone, FeatherData::ingredient)))
                .or(() -> feathers.get(head.unwrapKey().map(key -> LegacyArrowParts.feather(key.identifier())).orElse(Feathers.FEATHER)).map(reference -> reference))
                .or(() -> feathers.get(Feathers.FEATHER).map(reference -> reference));
    }
}
