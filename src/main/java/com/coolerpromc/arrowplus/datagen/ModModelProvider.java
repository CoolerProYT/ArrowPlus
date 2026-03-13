package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Potion;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public static final TextureSlot LAYER3 = TextureSlot.create("layer3");
    public static final ModelTemplate FOUR_LAYERED_ITEM = ModelTemplates.createItem("generated", TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2, LAYER3);

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModels) {
        // Arrows
        generateArrow(itemModels, ModItems.ARROW_PLUS);

        // Sticks
        itemModels.itemModelOutput.accept(ModItems.COPPER_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.COPPER_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));
        itemModels.itemModelOutput.accept(ModItems.IRON_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.IRON_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));
        itemModels.itemModelOutput.accept(ModItems.GOLD_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.GOLD_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));
        itemModels.itemModelOutput.accept(ModItems.DIAMOND_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.DIAMOND_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));
        itemModels.itemModelOutput.accept(ModItems.EMERALD_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.EMERALD_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));
        itemModels.itemModelOutput.accept(ModItems.NETHERITE_STICK, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.NETHERITE_STICK, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick"))), itemModels.modelOutput), new StickTintSource(-1)));

        // Feather
        itemModels.itemModelOutput.accept(ModItems.GILDED_FEATHER, ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.GILDED_FEATHER, TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/feather"))), itemModels.modelOutput), new FeatherTintSource(-1)));

        // Vanilla override
        itemModels.itemModelOutput.accept(Items.ARROW, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TextureMapping.getItemTexture(Items.ARROW).sprite(), TextureMapping.layer0(new Material(getModelLocation(Items.ARROW, ""))), itemModels.modelOutput)));
        generateBow(itemModels, Items.BOW);
    }

    private void generateArrow(ItemModelGenerators itemModels, Item item) {
        TextureMapping baseMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, new Material(ArrowPlus.id("item/arrow_stick")))
                .put(TextureSlot.LAYER1, new Material(ArrowPlus.id("item/arrow_head")))
                .put(TextureSlot.LAYER2, new Material(ArrowPlus.id("item/arrow_feather")));

        TextureMapping tippedMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, new Material(ArrowPlus.id("item/arrow_stick")))
                .put(TextureSlot.LAYER1, new Material(ArrowPlus.id("item/arrow_head")))
                .put(TextureSlot.LAYER2, new Material(ArrowPlus.id("item/arrow_feather")))
                .put(LAYER3, new Material(ArrowPlus.id("item/arrow_tipped")));

        Identifier base = ModelTemplates.THREE_LAYERED_ITEM.create(getModelLocation(item, ""), baseMapping, itemModels.modelOutput);
        Identifier tipped = FOUR_LAYERED_ITEM.create(getModelLocation(item, "_tipped"), tippedMapping, itemModels.modelOutput);

        itemModels.itemModelOutput.accept(item, new ConditionalItemModel.Unbaked(
                Optional.empty(),
                new TippedCondition(),
                ItemModelUtils.tintedModel(tipped, new StickTintSource(-1), new ArrowTintSource(0xFFFFFFFF), new FeatherTintSource(-1), new Potion(-1)),
                ItemModelUtils.tintedModel(base, new StickTintSource(-1), new ArrowTintSource(0xFFFFFFFF), new FeatherTintSource(-1)))
        );
    }

    public void generateBow(ItemModelGenerators itemModels, Item bowItem) {
        ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem));
        ItemModel.Unbaked itemmodel$unbaked1 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_0"), new Constant(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        ItemModel.Unbaked itemmodel$unbaked2 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_1"), new Constant(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        ItemModel.Unbaked itemmodel$unbaked3 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_2"), new Constant(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        itemModels.itemModelOutput.accept(
                bowItem,
                ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, itemmodel$unbaked1, ItemModelUtils.override(itemmodel$unbaked2, 0.65F), ItemModelUtils.override(itemmodel$unbaked3, 0.9F)), itemmodel$unbaked));
    }

    private Identifier createLayeredItemModel(ItemModelGenerators itemModels, Item item, String suffix) {
        return ModelTemplates.createItem("bow", TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2).create(TextureMapping.getItemTexture(item, suffix).sprite(), TextureMapping.layered(new Material(getModelLocation(item, suffix)), new Material(getModelLocation(item, suffix + "_head")), new Material(getModelLocation(item, suffix + "_stick"))), itemModels.modelOutput);
    }

    private Identifier getModelLocation(Item item, String suffix) {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/" + BuiltInRegistries.ITEM.getKey(item).getPath() + suffix);
    }
}
