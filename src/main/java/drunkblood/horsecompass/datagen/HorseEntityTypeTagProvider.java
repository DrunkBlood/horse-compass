package drunkblood.horsecompass.datagen;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import drunkblood.horsecompass.HorseCompass;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;

public class HorseEntityTypeTagProvider extends EntityTypeTagsProvider{
    public HorseEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, HorseCompass.MODID);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        this.tag(HorseCompass.TRACKABLE_BY)
            .addOptional(EntityType.HORSE)
            .addOptional(EntityType.DONKEY)
            .addOptional(EntityType.MULE)
            .addOptional(EntityType.CAMEL)
            .addOptional(EntityType.SKELETON_HORSE)
            .addOptional(EntityType.ZOMBIE_HORSE)
            ;
    }
    
}
