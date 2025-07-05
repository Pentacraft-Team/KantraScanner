package scanner;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import scanner.client.ClientEvents;
import scanner.network.ModNetworking;

public class ModEvents {

    public static void registerEvents(IEventBus modEventBus) {

        modEventBus.addListener(ModNetworking::register);


        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            NeoForge.EVENT_BUS.addListener(ModNetworking::onPlayerLogin);
            NeoForge.EVENT_BUS.addListener(ModNetworking::onBlockBreak);
        }


        if (FMLEnvironment.dist == Dist.CLIENT) {

            modEventBus.addListener(ClientEvents::onClientSetup);
            modEventBus.addListener(ClientEvents::onRegisterKeyBindings);

            NeoForge.EVENT_BUS.addListener(ClientEvents::onBlockBreak);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onClientTick);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onRenderLevelStage);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onMousePress);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onMouseRelease);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onRenderGuiOverlay);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onClientDisconnect);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onClientClose);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onKeyInput);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onRightClickBlock);
            NeoForge.EVENT_BUS.addListener(ClientEvents::onRenderGuiX);
        }
    }
}
