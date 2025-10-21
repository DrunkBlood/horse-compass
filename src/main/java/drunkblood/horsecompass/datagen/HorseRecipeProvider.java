package drunkblood.horsecompass.datagen;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import drunkblood.horsecompass.HorseCompass;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class HorseRecipeProvider extends RecipeProvider{

    protected HorseRecipeProvider(Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, HorseCompass.HORSE_COMPASS.get())
            .pattern("chc")
            .pattern("hoh")
            .pattern("chc")
            .define('c', Items.CARROT)
            .define('h', Items.HAY_BLOCK)
            .define('o', Items.COMPASS)
            .unlockedBy("has_compass", this.has(Items.COMPASS))
            .save(this.output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(@Nonnull HolderLookup.Provider provider, @Nonnull RecipeOutput output) {
            return new HorseRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "HorseRecipeProvider";
        }
    }

}
