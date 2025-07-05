package scanner.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import scanner.ScannerMod;

public record XrayConfigPayload(
        boolean isXrayEnabled,
        int xrayRange,
        int ticksToRender,
        int holdDurationMs
) implements CustomPacketPayload {

    public static final Type<XrayConfigPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ScannerMod.MODID, "config"));

    public static final StreamCodec<ByteBuf, XrayConfigPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, XrayConfigPayload::isXrayEnabled,
            ByteBufCodecs.INT, XrayConfigPayload::xrayRange,
            ByteBufCodecs.INT, XrayConfigPayload::ticksToRender,
            ByteBufCodecs.INT, XrayConfigPayload::holdDurationMs,
            XrayConfigPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
