package drunkblood.horsecompass.datagen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import drunkblood.horsecompass.HorseCompass;
import drunkblood.horsecompass.HorseCompassItem;
import drunkblood.horsecompass.client.HorseCompassAngle;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState.CompassTarget;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class HorseModelProvider extends ModelProvider{

    public HorseModelProvider(PackOutput output) {
        super(output, HorseCompass.MODID);
    }

    @Override
    protected void registerModels(@Nonnull BlockModelGenerators blockModels, @Nonnull ItemModelGenerators itemModels) {
      for (int i = 0; i < HorseCompassItem.SCALE; i++) {
        String suffix = String.format("_%02d", i);
        ModelTemplates.FLAT_ITEM.create(
          ModelLocationUtils.getModelLocation(HorseCompass.HORSE_COMPASS.get(), suffix),
          TextureMapping.layer0(TextureMapping.getItemTexture(HorseCompass.HORSE_COMPASS.get(), suffix)),
          itemModels.modelOutput);
      }
      itemModels.itemModelOutput.accept(
          HorseCompass.HORSE_COMPASS.get(),
          new ConditionalItemModel.Unbaked(
              new HasComponent(HorseCompass.HORSE_TRACKER.get(), false), 
              // on true use horse locator
              new RangeSelectItemModel.Unbaked(new HorseCompassAngle(true), HorseCompassItem.SCALE, CreateCompassEntries(), Optional.empty()), 
              // on false use compass random
              new RangeSelectItemModel.Unbaked(new CompassAngle(true, CompassTarget.NONE), HorseCompassItem.SCALE, CreateCompassEntries(), Optional.empty())
          )
      );
    }

    private List<RangeSelectItemModel.Entry> CreateCompassEntries(){
      List<RangeSelectItemModel.Entry> list = new ArrayList<RangeSelectItemModel.Entry>();
      int modelFileCounter = 16;
      list.add(new RangeSelectItemModel.Entry(0.0f, CreateCompassModel(modelFileCounter)));
      for(int i = 0; i < HorseCompassItem.SCALE; ++i){
        modelFileCounter = (modelFileCounter + 1) % HorseCompassItem.SCALE;
        list.add(new RangeSelectItemModel.Entry(i + 0.5f, CreateCompassModel(modelFileCounter)));
      }
      return list;
    }

    private ItemModel.Unbaked CreateCompassModel(int index){
      ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(HorseCompass.HORSE_COMPASS.get(), String.format("_%02d", index));
      return new BlockModelWrapper.Unbaked(modelLocation, Collections.emptyList());
    }
}
