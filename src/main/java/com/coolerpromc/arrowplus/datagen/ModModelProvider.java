package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.model.*;
import com.coolerpromc.arrowplus.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ConditionItemModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.render.item.tint.ConstantTintSource;
import net.minecraft.client.render.item.tint.PotionTintSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public static final TextureKey LAYER3 = TextureKey.of("layer3");
    public static final Model FOUR_LAYERED_ITEM = Models.item("generated", TextureKey.LAYER0, TextureKey.LAYER1, TextureKey.LAYER2, LAYER3);

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModels) {
        // Arrows
        generateArrow(itemModels, ModItems.ARROW_PLUS);

        // Sticks
        itemModels.output.accept(ModItems.COPPER_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.COPPER_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));
        itemModels.output.accept(ModItems.IRON_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.IRON_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));
        itemModels.output.accept(ModItems.GOLD_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.GOLD_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));
        itemModels.output.accept(ModItems.DIAMOND_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.DIAMOND_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));
        itemModels.output.accept(ModItems.EMERALD_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.EMERALD_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));
        itemModels.output.accept(ModItems.NETHERITE_STICK, ItemModels.tinted(Models.GENERATED.upload(ModItems.NETHERITE_STICK, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/stick")), itemModels.modelCollector), new StickTintSource(-1)));

        // Feather
        itemModels.output.accept(ModItems.GILDED_FEATHER, ItemModels.tinted(Models.GENERATED.upload(ModItems.GILDED_FEATHER, TextureMap.layer0(Identifier.of(ArrowPlus.MODID, "item/feather")), itemModels.modelCollector), new FeatherTintSource(-1)));

        // Vanilla override
        itemModels.output.accept(Items.ARROW, ItemModels.basic(Models.GENERATED.upload(TextureMap.getId(Items.ARROW), TextureMap.layer0(getModelLocation(Items.ARROW, "")), itemModels.modelCollector)));
        generateBow(itemModels, Items.BOW);
    }

    private void generateArrow(ItemModelGenerator itemModels, Item item) {
        TextureMap baseMapping = new TextureMap()
                .put(TextureKey.LAYER0, ArrowPlus.id("item/arrow_stick"))
                .put(TextureKey.LAYER1, ArrowPlus.id("item/arrow_head"))
                .put(TextureKey.LAYER2, ArrowPlus.id("item/arrow_feather"));

        TextureMap tippedMapping = new TextureMap()
                .put(TextureKey.LAYER0, ArrowPlus.id("item/arrow_stick"))
                .put(TextureKey.LAYER1, ArrowPlus.id("item/arrow_head"))
                .put(TextureKey.LAYER2, ArrowPlus.id("item/arrow_feather"))
                .put(LAYER3, ArrowPlus.id("item/arrow_tipped"));

        Identifier base = Models.GENERATED_THREE_LAYERS.upload(getModelLocation(item, ""), baseMapping, itemModels.modelCollector);
        Identifier tipped = FOUR_LAYERED_ITEM.upload(getModelLocation(item, "_tipped"), tippedMapping, itemModels.modelCollector);

        itemModels.output.accept(item, new ConditionItemModel.Unbaked(
                new TippedCondition(),
                ItemModels.tinted(tipped, new StickTintSource(-1), new ArrowTintSource(0xFFFFFFFF), new FeatherTintSource(-1), new PotionTintSource(-1)),
                ItemModels.tinted(base, new StickTintSource(-1), new ArrowTintSource(0xFFFFFFFF), new FeatherTintSource(-1)))
        );
    }

    public void generateBow(ItemModelGenerator itemModels, Item bowItem) {
        ItemModel.Unbaked itemmodel$unbaked = ItemModels.basic(ModelIds.getItemModelId(bowItem));
        ItemModel.Unbaked itemmodel$unbaked1 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_0"), new ConstantTintSource(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        ItemModel.Unbaked itemmodel$unbaked2 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_1"), new ConstantTintSource(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        ItemModel.Unbaked itemmodel$unbaked3 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_2"), new ConstantTintSource(-1), new BowTintSource(-1), new BowStickTintSource(0xFF886627));
        itemModels.output.accept(
                bowItem,
                ItemModels.condition(ItemModels.usingItemProperty(), ItemModels.rangeDispatch(new UseDurationProperty(false), 0.05F, itemmodel$unbaked1, ItemModels.rangeDispatchEntry(itemmodel$unbaked2, 0.65F), ItemModels.rangeDispatchEntry(itemmodel$unbaked3, 0.9F)), itemmodel$unbaked));
    }

    private Identifier createLayeredItemModel(ItemModelGenerator itemModels, Item item, String suffix) {
        return Models.item("bow", TextureKey.LAYER0, TextureKey.LAYER1, TextureKey.LAYER2).upload(TextureMap.getSubId(item, suffix), TextureMap.layered(getModelLocation(item, suffix), getModelLocation(item, suffix + "_head"), getModelLocation(item, suffix + "_stick")), itemModels.modelCollector);
    }

    private Identifier getModelLocation(Item item, String suffix) {
        return Identifier.of(ArrowPlus.MODID, "item/" + Registries.ITEM.getId(item).getPath() + suffix);
    }
}
