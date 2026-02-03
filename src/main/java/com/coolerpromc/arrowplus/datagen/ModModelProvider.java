package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Potion;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public static final TextureSlot LAYER3 = TextureSlot.create("layer3");
    public static final ModelTemplate FOUR_LAYERED_ITEM = ModelTemplates.createItem("generated", TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2, LAYER3);

    public ModModelProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected ItemModelGenerators getItemModelGenerators(ItemInfoCollector items, SimpleModelCollector models) {
        return new ItemModelGenerators(items, models){
            @Override
            public void run() {
                super.run();
                // Arrows
                generateArrow(this, ModItems.ARROW_PLUS.get());

                // Sticks
                this.itemModelOutput.accept(ModItems.COPPER_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.COPPER_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));
                this.itemModelOutput.accept(ModItems.IRON_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.IRON_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));
                this.itemModelOutput.accept(ModItems.GOLD_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.GOLD_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));
                this.itemModelOutput.accept(ModItems.DIAMOND_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.DIAMOND_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));
                this.itemModelOutput.accept(ModItems.EMERALD_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.EMERALD_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));
                this.itemModelOutput.accept(ModItems.NETHERITE_STICK.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.NETHERITE_STICK.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/stick")), this.modelOutput), new StickTintSource(-1)));

                // Feather
                this.itemModelOutput.accept(ModItems.GILDED_FEATHER.get(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModItems.GILDED_FEATHER.get(), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/feather")), this.modelOutput), new FeatherTintSource(-1)));

                // Vanilla override
                this.itemModelOutput.accept(Items.ARROW, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TextureMapping.getItemTexture(Items.ARROW), TextureMapping.layer0(getModelLocation(Items.ARROW, "")), this.modelOutput)));
                generateBow(this, Items.BOW);
            }

            private void generateArrow(ItemModelGenerators itemModels, Item item) {
                TextureMapping baseMapping = new TextureMapping()
                        .put(TextureSlot.LAYER0, ArrowPlus.id("item/arrow_stick"))
                        .put(TextureSlot.LAYER1, ArrowPlus.id("item/arrow_head"))
                        .put(TextureSlot.LAYER2, ArrowPlus.id("item/arrow_feather"));

                TextureMapping tippedMapping = new TextureMapping()
                        .put(TextureSlot.LAYER0, ArrowPlus.id("item/arrow_stick"))
                        .put(TextureSlot.LAYER1, ArrowPlus.id("item/arrow_head"))
                        .put(TextureSlot.LAYER2, ArrowPlus.id("item/arrow_feather"))
                        .put(LAYER3, ArrowPlus.id("item/arrow_tipped"));

                ResourceLocation base = ModelTemplates.THREE_LAYERED_ITEM.create(getModelLocation(item, ""), baseMapping, this.modelOutput);
                ResourceLocation tipped = FOUR_LAYERED_ITEM.create(getModelLocation(item, "_tipped"), tippedMapping, this.modelOutput);

                this.itemModelOutput.accept(item, new ConditionalItemModel.Unbaked(
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
                this.itemModelOutput.accept(
                        bowItem,
                        ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, itemmodel$unbaked1, ItemModelUtils.override(itemmodel$unbaked2, 0.65F), ItemModelUtils.override(itemmodel$unbaked3, 0.9F)), itemmodel$unbaked));
            }

            private ResourceLocation createLayeredItemModel(ItemModelGenerators itemModels, Item item, String suffix) {
                return ModelTemplates.createItem("bow", TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2).create(TextureMapping.getItemTexture(item, suffix), TextureMapping.layered(getModelLocation(item, suffix), getModelLocation(item, suffix + "_head"), getModelLocation(item, suffix + "_stick")), this.modelOutput);
            }
        };
    }

    @Override
    protected Stream<Item> getKnownItems() {
        return BuiltInRegistries.ITEM.listElements().filter(itemReference -> Optional.of(BuiltInRegistries.ITEM.getKey(itemReference.value())).filter(ResourceLocation -> ResourceLocation.getNamespace().equals(ArrowPlus.MODID)).isPresent()).map(Holder::value);
    }

    @Override
    protected Stream<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.listElements().filter(itemReference -> Optional.of(BuiltInRegistries.BLOCK.getKey(itemReference.value())).filter(ResourceLocation -> ResourceLocation.getNamespace().equals(ArrowPlus.MODID)).isPresent()).map(Holder::value);
    }

    private ResourceLocation getModelLocation(Item item, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/" + BuiltInRegistries.ITEM.getKey(item).getPath() + suffix);
    }
}
