package drunkblood.horsecompass.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

public class HorseCompassAngle implements RangeSelectItemModelProperty{
    public static final MapCodec<HorseCompassAngle> MAP_CODEC = HorseCompassAngleState.MAP_CODEC.xmap(HorseCompassAngle::new, compassAngle -> compassAngle.state);
    private final HorseCompassAngleState state;

    public HorseCompassAngle(boolean wooble){
        this(new HorseCompassAngleState(wooble));
    }

    private HorseCompassAngle(HorseCompassAngleState state){
        this.state = state;
    }

    @Override
    public float get(@Nonnull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable ItemOwner owner, int seed) {
        if(clientLevel == null) return 0.0f;
        return state.calculate(itemStack, clientLevel, seed, owner);
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }

}
