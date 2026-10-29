package com.coolerpromc.arrowplus.mixin;

import com.coolerpromc.arrowplus.datapack.ArrowMigrationCodec;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStackTemplate.class)
public class ItemStackTemplateMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"))
    private static MapCodec<ItemStackTemplate> arrowplus$migrateTemplates(MapCodec<ItemStackTemplate> original) {
        return ArrowMigrationCodec.wrapTemplate(original);
    }
}
