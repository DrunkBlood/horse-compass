package drunkblood.horsecompass;

import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;

public record HorseTracker(UUID entityId) {
    public static final Codec<HorseTracker> CODEC = RecordCodecBuilder.create((horseTracker ->
        horseTracker.group(
            UUIDUtil.CODEC.fieldOf("entityId").forGetter(HorseTracker::entityId)
        ).apply(horseTracker, HorseTracker::new)
    ));
    public static final StreamCodec<ByteBuf, HorseTracker> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, HorseTracker::entityId,
            HorseTracker::new
    );
}
