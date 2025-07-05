package scanner;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import scanner.item.BlockScannerItem;
import scanner.item.ScannerGlassesItem;

public class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ScannerMod.MODID);

    public static final DeferredItem<Item> BLOCK_SCANNER = ITEMS.register("scanner",
            () -> new BlockScannerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CUSTOM_HELMET = ITEMS.register("scanner_glasses",
            () -> new ScannerGlassesItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).setNoRepair()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
