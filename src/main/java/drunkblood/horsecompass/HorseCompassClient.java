package drunkblood.horsecompass;

import drunkblood.horsecompass.client.HorseCompassAngle;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = HorseCompass.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HorseCompass.MODID, value = Dist.CLIENT)
public class HorseCompassClient {
    public HorseCompassClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerRangeProperties(RegisterRangeSelectItemModelPropertyEvent event){
        event.register(
            ResourceLocation.fromNamespaceAndPath(HorseCompass.MODID, "compass_angle"), 
            HorseCompassAngle.MAP_CODEC
        );
    }
}
