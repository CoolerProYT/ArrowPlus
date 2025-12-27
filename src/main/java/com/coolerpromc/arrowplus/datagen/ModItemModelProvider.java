package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import com.coolerpromc.arrowplus.item.custom.ModFeatherItem;
import com.coolerpromc.arrowplus.item.custom.ModStickItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArrowPlus.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Arrows
        generateArrowPlus(ModItems.ARROW_PLUS);

        // Sticks
        generateStick(ModItems.COPPER_STICK);
        generateStick(ModItems.IRON_STICK);
        generateStick(ModItems.GOLD_STICK);
        generateStick(ModItems.DIAMOND_STICK);
        generateStick(ModItems.EMERALD_STICK);
        generateStick(ModItems.NETHERITE_STICK);

        // Feathers
        generateFeather(ModItems.GILDED_FEATHER);

        // Vanilla Override
        generateArrow(Items.ARROW);
        generateBow(Items.BOW);
    }

    public void generateArrowPlus(DeferredItem<ModArrowItem> item){
        getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ArrowPlus.id("item/arrow_stick"))
                .texture("layer1", ArrowPlus.id("item/arrow_head"))
                .texture("layer2", ArrowPlus.id("item/arrow_feather"))
                .override()
                .predicate(ArrowPlus.id("tipped"), 1)
                .model(new ModelFile.UncheckedModelFile(
                        ArrowPlus.id("item/" + item.getId().getPath() + "_tipped")
                ))
                .end();

        getBuilder(item.getId().getPath() + "_tipped")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ArrowPlus.id("item/arrow_stick"))
                .texture("layer1", ArrowPlus.id("item/arrow_head"))
                .texture("layer2", ArrowPlus.id("item/arrow_feather"))
                .texture("layer3", ArrowPlus.id("item/arrow_tipped"));
    }

    public void generateStick(DeferredItem<ModStickItem> item){
        getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ArrowPlus.id("item/stick"));
    }

    public void generateFeather(DeferredItem<ModFeatherItem> item){
        getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ArrowPlus.id("item/feather"));
    }

    public void generateArrow(Item item){
        getBuilder(BuiltInRegistries.ITEM.getKey(item).toString())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", getModelLocation(item, ""));
    }

    public void generateBow(Item bowItem) {
        getBuilder(BuiltInRegistries.ITEM.getKey(bowItem).toString())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ResourceLocation.withDefaultNamespace("item/" + BuiltInRegistries.ITEM.getKey(bowItem).getPath()))
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(-80, 260, -40)
                .translation(-1, -2, 2.5f)
                .scale(0.9F)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(-80, -280, 40)
                .translation(-1, -2, 2.5f)
                .scale(0.9F)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68F)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, -25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68F)
                .end()
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .model(new ModelFile.UncheckedModelFile(getModelLocation(bowItem, "_pulling_0")))
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 0.65F)
                .model(new ModelFile.UncheckedModelFile(getModelLocation(bowItem, "_pulling_1")))
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 0.9F)
                .model(new ModelFile.UncheckedModelFile(getModelLocation(bowItem, "_pulling_2")))
                .end();

        getBuilder(getModelLocation(bowItem, "_pulling_0").toString())
                .parent(getExistingFile(mcLoc("item/bow")))
                .texture("layer0", getModelLocation(bowItem, "_pulling_0"))
                .texture("layer1", getModelLocation(bowItem, "_pulling_0_head"))
                .texture("layer2", getModelLocation(bowItem, "_pulling_0_stick"));

        getBuilder(getModelLocation(bowItem, "_pulling_1").toString())
                .parent(getExistingFile(mcLoc("item/bow")))
                .texture("layer0", getModelLocation(bowItem, "_pulling_1"))
                .texture("layer1", getModelLocation(bowItem, "_pulling_1_head"))
                .texture("layer2", getModelLocation(bowItem, "_pulling_1_stick"));

        getBuilder(getModelLocation(bowItem, "_pulling_2").toString())
                .parent(getExistingFile(mcLoc("item/bow")))
                .texture("layer0", getModelLocation(bowItem, "_pulling_2"))
                .texture("layer1", getModelLocation(bowItem, "_pulling_2_head"))
                .texture("layer2", getModelLocation(bowItem, "_pulling_2_stick"));
    }

    private ResourceLocation getModelLocation(Item item, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/" + BuiltInRegistries.ITEM.getKey(item).getPath() + suffix);
    }
}
