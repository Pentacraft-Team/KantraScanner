package scanner;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ScannerMod.MODID)
public class ScannerMod {
    public static final String MODID = "scanner";

    public ScannerMod(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModEvents.registerEvents(modEventBus);
    }
}
