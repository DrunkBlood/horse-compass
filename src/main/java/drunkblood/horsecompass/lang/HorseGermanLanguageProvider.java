package drunkblood.horsecompass.lang;

import drunkblood.horsecompass.HorseCompass;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class HorseGermanLanguageProvider extends LanguageProvider{

    public HorseGermanLanguageProvider(PackOutput output) {
        super(output, HorseCompass.MODID, "de_de");
    }

    @Override
    protected void addTranslations() {
        this.addItem(HorseCompass.HORSE_COMPASS, "Pferdekompass");
    }
}
