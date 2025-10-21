package drunkblood.horsecompass;

import javax.annotation.Nonnull;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HorseCompassItem extends CompassItem{
    public static final int SCALE = 32;
    public HorseCompassItem(Item.Properties prop){
        super(prop);
    }
    
    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return stack.has(HorseCompass.HORSE_TRACKER.get()) || super.isFoil(stack);
    }
}
