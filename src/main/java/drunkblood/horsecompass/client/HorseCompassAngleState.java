package drunkblood.horsecompass.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import drunkblood.horsecompass.HorseCompass;
import drunkblood.horsecompass.HorseTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HorseCompassAngleState extends NeedleDirectionHelper{
    public static final MapCodec<HorseCompassAngleState> MAP_CODEC = RecordCodecBuilder.mapCodec(state ->
        state.group(
            Codec.BOOL.optionalFieldOf("wobble", true).forGetter(HorseCompassAngleState::wobble)
        ).apply(state, HorseCompassAngleState::new)
    );
    private final NeedleDirectionHelper.Wobbler wobbler = this.newWobbler(0.8F);
    private final NeedleDirectionHelper.Wobbler noTargetWobbler = this.newWobbler(0.8F);
    private final RandomSource random = RandomSource.create();

    protected HorseCompassAngleState(boolean wobble) {
        super(wobble);
    }

    @Override
    protected float calculate(@Nonnull ItemStack itemStack, @Nonnull ClientLevel clientLevel, int seed, @Nullable ItemOwner owner) {
        long i = clientLevel.getGameTime();
        if(owner == null) return getRandomlySpinningRotation(seed, i);
        HorseTracker tracker = (HorseTracker) itemStack.get(HorseCompass.HORSE_TRACKER);
        if(tracker == null) return getRandomlySpinningRotation(seed, i);
        Entity entity = clientLevel.getEntity(tracker.entityId());
        if( entity == null 
            || entity.level().dimension() != owner.level().dimension()
            ) return getRandomlySpinningRotation(seed, i);
        return getRotationTowardsCompassTarget(owner, i, entity.getEyePosition());
    }

    private float getRandomlySpinningRotation(int seed, long gameTime) {
        if (this.noTargetWobbler.shouldUpdate(gameTime)) {
            this.noTargetWobbler.update(gameTime, this.random.nextFloat());
        }

        float f = this.noTargetWobbler.rotation() + (float)hash(seed) / 2.1474836E9F;
        return Mth.positiveModulo(f, 1.0F);
    }

    private float getRotationTowardsCompassTarget(ItemOwner owner, long gameTime, Vec3 entityEye) {
        float f = (float)getAngleFromEntityToPos(owner, entityEye);
        float f1 = getWrappedVisualRotationY(owner);
        LivingEntity var9 = owner.asLivingEntity();
        float f2;
        if (var9 instanceof Player player) {
            if (player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
                if (this.wobbler.shouldUpdate(gameTime)) {
                    this.wobbler.update(gameTime, 0.5F - (f1 - 0.25F));
                }

                f2 = f + this.wobbler.rotation();
                return Mth.positiveModulo(f2, 1.0F);
            }
        }

        f2 = 0.5F - (f1 - 0.25F - f);
        return Mth.positiveModulo(f2, 1.0F);
    }

    private static double getAngleFromEntityToPos(ItemOwner owner, Vec3 entityEye) {
        Vec3 vec31 = owner.position();
        return Math.atan2(entityEye.z() - vec31.z(), entityEye.x() - vec31.x()) / 6.2831854820251465;
    }
        
    private static float getWrappedVisualRotationY(ItemOwner owner) {
        return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
    }

    private static int hash(int seed) {
        return seed * 1327217883;
    }
}
