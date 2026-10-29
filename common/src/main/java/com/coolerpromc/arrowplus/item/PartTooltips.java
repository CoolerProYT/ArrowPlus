package com.coolerpromc.arrowplus.item;

import com.coolerpromc.arrowplus.datapack.ArrowParts;
import com.coolerpromc.arrowplus.datapack.arrow.ArrowData;
import com.coolerpromc.arrowplus.datapack.feather.FeatherData;
import com.coolerpromc.arrowplus.datapack.stick.StickData;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class PartTooltips {
    private static final double VANILLA_DAMAGE = 2.0;

    private PartTooltips() {
    }

    private static boolean shiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_RSHIFT);
    }

    public static boolean showDetails(Consumer<Component> tooltips) {
        if (shiftDown()){
            return true;
        }
        tooltips.accept(Component.translatable("tooltip.arrowplus.hint",
                Component.literal("[Shift]").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
        return false;
    }

    public static void addParts(StickData stick, FeatherData feather, Consumer<Component> tooltips) {
        tooltips.accept(Component.translatable("tooltip.arrowplus.parts", Component.translatable(stick.translationKey()),
                Component.translatable(feather.translationKey())).withStyle(ChatFormatting.GRAY));
    }

    public static void addArrow(ArrowData head, StickData stick, FeatherData feather, boolean infinity, Consumer<Component> tooltips) {
        addParts(stick, feather, tooltips);
        addArrowStats(head.baseDamage(), head.flame(), head.effectInstances(), stick, feather, infinity, tooltips);
    }

    public static void addVanillaArrow(boolean infinity, Consumer<Component> tooltips) {
        addArrowStats(VANILLA_DAMAGE, false, List.of(), StickData.VANILLA, FeatherData.VANILLA, infinity, tooltips);
    }

    private static void addArrowStats(double damage, boolean flame, List<MobEffectInstance> effects, StickData stick, FeatherData feather, boolean infinity, Consumer<Component> tooltips) {
        if (!shiftDown()){
            tooltips.accept(Component.translatable("tooltip.arrowplus.summary",
                    value(damage, VANILLA_DAMAGE, true, format(damage)),
                    value(stick.velocity(), 1, true, "×" + format(stick.velocity()))).withStyle(ChatFormatting.GRAY));
            showDetails(tooltips);
            return;
        }
        heading(Component.translatable("tooltip.arrowplus.section.combat"), tooltips);
        stat("damage", value(damage, VANILLA_DAMAGE, true, format(damage)), tooltips);
        if (flame){
            tooltips.accept(Component.translatable("tooltip.arrowplus.ignites").withStyle(ChatFormatting.GOLD));
        }
        stat("infinity", Component.translatable("tooltip.arrowplus." + (infinity ? "supported" : "unavailable"))
                .withStyle(infinity ? ChatFormatting.GREEN : ChatFormatting.GRAY), tooltips);

        heading(Component.translatable("tooltip.arrowplus.section.flight"), tooltips);
        stat("speed", value(stick.velocity(), 1, true, "×" + format(stick.velocity())), tooltips);
        double gravity = stick.gravity() * feather.gravityMultiplier() / StickData.DEFAULT_GRAVITY;
        stat("drop", value(gravity, 1, false, "×" + format(gravity)), tooltips);
        addDragAndSpread(feather, tooltips);
        addEffects(effects, tooltips);
    }

    public static void addHead(ArrowData head, Consumer<Component> tooltips) {
        stat("damage", value(head.baseDamage(), VANILLA_DAMAGE, true, format(head.baseDamage())), tooltips);
        if (head.flame()){
            tooltips.accept(Component.translatable("tooltip.arrowplus.ignites").withStyle(ChatFormatting.GOLD));
        }
        addEffects(head.effectInstances(), tooltips);
    }

    private static void addEffects(List<MobEffectInstance> effects, Consumer<Component> tooltips) {
        if (!effects.isEmpty()){
            heading(Component.translatable("tooltip.arrowplus.section.effects"), tooltips);
            PotionContents.addPotionTooltip(effects, tooltips, 1.0F, 20.0F);
        }
    }

    public static void addStick(StickData stick, Consumer<Component> tooltips, boolean withName) {
        if (withName){
            heading(Component.translatable(stick.translationKey()), tooltips);
        }
        stat("speed", value(stick.velocity(), 1, true, "×" + format(stick.velocity())), tooltips);
        double gravity = stick.gravity() / StickData.DEFAULT_GRAVITY;
        stat("drop", value(gravity, 1, false, "×" + format(gravity)), tooltips);
    }

    public static void addFeather(FeatherData feather, Consumer<Component> tooltips, boolean withName) {
        if (withName){
            heading(Component.translatable(feather.translationKey()), tooltips);
        }
        stat("drop_modifier", value(feather.gravityMultiplier(), 1, false, "×" + format(feather.gravityMultiplier())), tooltips);
        addDragAndSpread(feather, tooltips);
    }

    private static void addDragAndSpread(FeatherData feather, Consumer<Component> tooltips) {
        stat("air_loss", value(feather.airDrag(), FeatherData.DEFAULT_AIR_DRAG, true, percent(1 - feather.airDrag())), tooltips);
        stat("water_loss", value(feather.waterDrag(), FeatherData.DEFAULT_WATER_DRAG, true, percent(1 - feather.waterDrag())), tooltips);
        stat("spread", value(feather.accuracy(), 1, false, "×" + format(feather.accuracy())), tooltips);
    }

    private static void stat(String key, Component value, Consumer<Component> tooltips) {
        tooltips.accept(Component.translatable("tooltip.arrowplus.stat",
                Component.translatable("tooltip.arrowplus.stat." + key), value).withStyle(ChatFormatting.GRAY));
    }

    private static void heading(Component name, Consumer<Component> tooltips) {
        tooltips.accept(Component.empty());
        tooltips.accept(name.copy().withStyle(ChatFormatting.GOLD));
    }

    private static Component value(double number, double vanilla, boolean higherIsBetter, String text) {
        ChatFormatting color = Math.abs(number - vanilla) < 1.0E-9 ? ChatFormatting.WHITE
                : (number > vanilla) == higherIsBetter ? ChatFormatting.GREEN : ChatFormatting.RED;
        return Component.literal(text).withStyle(color);
    }

    public static void addMaterial(ItemStack stack, @Nullable HolderLookup.Provider registries, Consumer<Component> tooltips) {
        if (registries == null || stack.getItem() instanceof ModArrowItem || stack.getItem() instanceof ModStickItem || stack.getItem() instanceof ModFeatherItem){
            return;
        }
        Optional<Holder.Reference<ArrowData>> head = ArrowParts.findHead(stack, registries);
        Optional<Holder<StickData>> stick = ArrowParts.findStick(stack, registries);
        Optional<Holder<FeatherData>> feather = ArrowParts.findFeather(stack, registries);
        List<StickData> craftedSticks = ArrowParts.craftedSticks(stack, registries);
        List<FeatherData> craftedFeathers = ArrowParts.craftedFeathers(stack, registries);
        if (head.isEmpty() && stick.isEmpty() && feather.isEmpty() && craftedSticks.isEmpty() && craftedFeathers.isEmpty()){
            return;
        }

        if (!shiftDown()){
            tooltips.accept(Component.translatable("tooltip.arrowplus.part").withStyle(ChatFormatting.GRAY));
            showDetails(tooltips);
            return;
        }

        head.ifPresent(h -> {
            heading(Component.translatable("tooltip.arrowplus.part.head_header"), tooltips);
            addHead(h.value(), tooltips);
        });
        stick.ifPresent(s -> {
            heading(Component.translatable("tooltip.arrowplus.part.stick_header"), tooltips);
            addStick(s.value(), tooltips, false);
        });
        feather.ifPresent(f -> {
            heading(Component.translatable("tooltip.arrowplus.part.feather_header"), tooltips);
            addFeather(f.value(), tooltips, false);
        });
        craftedSticks.forEach(s -> {
            heading(Component.translatable("tooltip.arrowplus.part.crafted_header", Component.translatable(s.translationKey())), tooltips);
            addStick(s, tooltips, false);
        });
        craftedFeathers.forEach(f -> {
            heading(Component.translatable("tooltip.arrowplus.part.crafted_header", Component.translatable(f.translationKey())), tooltips);
            addFeather(f, tooltips, false);
        });
    }

    private static String format(double value) {
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private static String percent(double fraction) {
        return format(fraction * 100) + "%";
    }
}
