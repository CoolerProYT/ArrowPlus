package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.model.ArrowTintSource;
import com.coolerpromc.arrowplus.datagen.model.BowTintSource;
import com.coolerpromc.arrowplus.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.render.item.tint.ConstantTintSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModels) {
        itemModels.registerWithTintedLayer(ModItems.ARROW_PLUS, "_layer1", new ArrowTintSource(0xFFFFFFFF));
        itemModels.output.accept(Items.ARROW, ItemModels.basic(Models.GENERATED.upload(TextureMap.getId(Items.ARROW), TextureMap.layer0(getModelLocation(Items.ARROW, "")), itemModels.modelCollector)));
        generateBow(itemModels, Items.BOW);
    }

    public void generateBow(ItemModelGenerator itemModels, Item bowItem) {
        ItemModel.Unbaked itemmodel$unbaked = ItemModels.basic(ModelIds.getItemModelId(bowItem));
        ItemModel.Unbaked itemmodel$unbaked1 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_0"), new ConstantTintSource(-1), new BowTintSource(-1));
        ItemModel.Unbaked itemmodel$unbaked2 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_1"), new ConstantTintSource(-1), new BowTintSource(-1));
        ItemModel.Unbaked itemmodel$unbaked3 = ItemModels.tinted(this.createLayeredItemModel(itemModels, bowItem, "_pulling_2"), new ConstantTintSource(-1), new BowTintSource(-1));
        itemModels.output.accept(
                bowItem,
                ItemModels.condition(ItemModels.usingItemProperty(), ItemModels.rangeDispatch(new UseDurationProperty(false), 0.05F, itemmodel$unbaked1, ItemModels.rangeDispatchEntry(itemmodel$unbaked2, 0.65F), ItemModels.rangeDispatchEntry(itemmodel$unbaked3, 0.9F)), itemmodel$unbaked));
    }

    private Identifier createLayeredItemModel(ItemModelGenerator itemModels, Item item, String suffix) {
        return Models.GENERATED_TWO_LAYERS.upload(TextureMap.getSubId(item, suffix), TextureMap.layered(getModelLocation(item, suffix), getModelLocation(item, suffix + "_head")), itemModels.modelCollector);
    }

    private Identifier getModelLocation(Item item, String suffix) {
        return Identifier.of(ArrowPlus.MODID, "item/" + Registries.ITEM.getId(item).getPath() + suffix);
    }
}
