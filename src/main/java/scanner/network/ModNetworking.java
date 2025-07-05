package scanner.network;


import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import scanner.ScannerMod;
import scanner.client.ClientEvents;
import scanner.config.ConfigManager;
import scanner.config.ServerConfig;

public class ModNetworking {

    private static final ResourceLocation CONFIG = ResourceLocation.fromNamespaceAndPath(ScannerMod.MODID, "config");
    private static final ResourceLocation BLOCK_BREAK = ResourceLocation.fromNamespaceAndPath(ScannerMod.MODID, "block_break");

    public static void register(final RegisterPayloadHandlersEvent event) {

        event.registrar(CONFIG.toString())
                .playToClient(
                        XrayConfigPayload.TYPE,
                        XrayConfigPayload.STREAM_CODEC,
                        (payload, context) -> {
                            ConfigManager.setXrayEnabled(payload.isXrayEnabled());
                            ConfigManager.setXrayRange(payload.xrayRange());
                            ConfigManager.setTICKS_TO_RENDER(payload.ticksToRender());
                            ConfigManager.setHOLD_DURATION_MS(payload.holdDurationMs());
                        }
                );

        event.registrar(BLOCK_BREAK.toString())
                .playToClient(
                        BlockBreakPayload.TYPE,
                        BlockBreakPayload.STREAM_CODEC,
                        (payload, context) -> {
                            Level level = getClientLevel(payload.levelKey());
                            if (level != null) {
                                ClientEvents.highlightHandler.onBlockDestroyed(level, payload.pos());
                            }
                        }
                );
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerConfig.XraySettings cfg = ServerConfig.load();
            player.connection.send(new XrayConfigPayload(
                    cfg.isXrayEnabled,
                    cfg.xrayRange,
                    cfg.TICKS_TO_RENDER,
                    cfg.HOLD_DURATION_MS
            ));
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();


        BlockBreakPayload payload = new BlockBreakPayload(
                level.dimension(),
                pos
        );

        if (event.getPlayer() instanceof ServerPlayer player) {
            player.connection.send(payload);
        }

    }

    @OnlyIn(Dist.CLIENT)
    private static Level getClientLevel(ResourceKey<Level> levelKey) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().equals(levelKey)) {
            return mc.level;
        }
        return null;
    }
}
