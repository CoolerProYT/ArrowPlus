package com.coolerpromc.arrowplus.datapack;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrowMigrationCodecTest {
    @BeforeAll
    static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    private static ItemStackTemplate unboundTemplate(String id) {
        HolderOwner<Item> owner = new HolderOwner<>() {
        };
        Holder.Reference<Item> item = Holder.Reference.createStandAlone(owner, ResourceKey.create(Registries.ITEM, Identifier.parse(id)));
        return new ItemStackTemplate(item, 4, DataComponentPatch.EMPTY);
    }

    @Test
    void doesNotMaterializeUnboundTemplatesWithoutRegistryContext() {
        ItemStackTemplate template = unboundTemplate("minecraft:emerald");
        MapCodec<ItemStackTemplate> codec = ArrowMigrationCodec.wrapTemplate(MapCodec.unit(template));
        ItemStackTemplate decoded = codec.codec().parse(JsonOps.INSTANCE, new JsonObject()).getOrThrow();
        assertSame(template, decoded);
        assertFalse(decoded.item().areComponentsBound());
    }

    @Test
    void defersArrowTemplatesUntilItemComponentsAreBound() {
        ItemStackTemplate template = unboundTemplate("arrowplus:arrow_plus");
        assertSame(template, ArrowParts.migrate(template, null, null));
        assertEquals(4, template.count());
        assertTrue(template.components().isEmpty());
    }
}
