package com.coolerpromc.arrowplus.datagen;

import com.coolerpromc.arrowplus.ArrowPlus;
import com.coolerpromc.arrowplus.item.ModItems;
import com.coolerpromc.arrowplus.item.custom.ModArrowItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArrowPlus.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generateArrowPlus(ModItems.ARROW_PLUS);
        generateArrow(Items.ARROW);
        generateBow(Items.BOW);
    }

    public void generateArrowPlus(RegistryObject<ModArrowItem> item){
        getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", getModelLocation(item.get(), ""))
                .texture("layer1", getModelLocation(item.get(), "_layer1"));
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
                .texture("layer1", getModelLocation(bowItem, "_pulling_0_head"));

        getBuilder(getModelLocation(bowItem, "_pulling_1").toString())
                .parent(getExistingFile(mcLoc("item/bow")))
                .texture("layer0", getModelLocation(bowItem, "_pulling_1"))
                .texture("layer1", getModelLocation(bowItem, "_pulling_1_head"));

        getBuilder(getModelLocation(bowItem, "_pulling_2").toString())
                .parent(getExistingFile(mcLoc("item/bow")))
                .texture("layer0", getModelLocation(bowItem, "_pulling_2"))
                .texture("layer1", getModelLocation(bowItem, "_pulling_2_head"));
    }

    private ResourceLocation getModelLocation(Item item, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(ArrowPlus.MODID, "item/" + BuiltInRegistries.ITEM.getKey(item).getPath() + suffix);
    }
}
