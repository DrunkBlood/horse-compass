package drunkblood.horsecompass.lang;

import drunkblood.horsecompass.HorseCompass;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class HorseEnglishLanguageProvider extends LanguageProvider{

    public HorseEnglishLanguageProvider(PackOutput output) {
        super(output, HorseCompass.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(HorseCompass.HORSE_COMPASS, "Horse Compass");
    }
}
