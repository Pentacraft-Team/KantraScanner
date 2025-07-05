package scanner.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import scanner.ScannerMod;

public record BlockBreakPayload(
        ResourceKey<Level> levelKey,
        BlockPos pos
) implements CustomPacketPayload {
    public static final Type<BlockBreakPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ScannerMod.MODID, "block_break"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, BlockBreakPayload> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeResourceLocation(packet.levelKey().location());
                buf.writeBlockPos(packet.pos());
            },
            buf -> {
                ResourceKey<Level> levelKey = ResourceKey.create(
                        Registries.DIMENSION,
                        buf.readResourceLocation()
                );
                return new BlockBreakPayload(levelKey, buf.readBlockPos());
            }
    );
}
