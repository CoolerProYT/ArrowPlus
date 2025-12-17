package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.datagen.model.ArrowTintSource;
import com.coolerpromc.arrowplus.datagen.model.BowTintSource;
import com.coolerpromc.arrowplus.item.ModItems;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected ItemModelGenerators getItemModelGenerators(ItemInfoCollector items, SimpleModelCollector models) {
        return new ItemModelGenerators(items, models){
            @Override
            public void run() {
                this.generateItemWithTintedOverlay(ModItems.ARROW_PLUS.get(), "_layer1", new ArrowTintSource(0xFFFFFFFF));
                this.itemModelOutput.accept(Items.ARROW, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TextureMapping.getItemTexture(Items.ARROW), TextureMapping.layer0(getModelLocation(Items.ARROW, "")), this.modelOutput)));
                generateBow(this, Items.BOW);
            }

            public void generateBow(ItemModelGenerators itemModels, Item bowItem) {
                ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem));
                ItemModel.Unbaked itemmodel$unbaked1 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_0"), new Constant(-1), new BowTintSource(-1));
                ItemModel.Unbaked itemmodel$unbaked2 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_1"), new Constant(-1), new BowTintSource(-1));
                ItemModel.Unbaked itemmodel$unbaked3 = ItemModelUtils.tintedModel(this.createLayeredItemModel(itemModels, bowItem, "_pulling_2"), new Constant(-1), new BowTintSource(-1));
                this.itemModelOutput.accept(
                        bowItem,
                        ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, itemmodel$unbaked1, ItemModelUtils.override(itemmodel$unbaked2, 0.65F), ItemModelUtils.override(itemmodel$unbaked3, 0.9F)), itemmodel$unbaked));
            }

            private Identifier createLayeredItemModel(ItemModelGenerators itemModels, Item item, String suffix) {
                return ModelTemplates.TWO_LAYERED_ITEM.create(TextureMapping.getItemTexture(item, suffix), TextureMapping.layered(getModelLocation(item, suffix), getModelLocation(item, suffix + "_head")), this.modelOutput);
            }
        };
    }

    private Identifier getModelLocation(Item item, String suffix) {
        return Identifier.fromNamespaceAndPath(ArrowPlus.MODID, "item/" + BuiltInRegistries.ITEM.getKey(item).getPath() + suffix);
    }


}
