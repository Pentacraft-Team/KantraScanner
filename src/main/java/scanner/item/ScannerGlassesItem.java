package scanner.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.config.ConfigManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScannerGlassesItem extends ArmorItem {

    @OnlyIn(Dist.CLIENT)
    private Map<Block, Boolean> blocksToHighlight;

    @OnlyIn(Dist.CLIENT)
    private void initClient() {
        if (blocksToHighlight == null) {
            blocksToHighlight = ConfigManager.getBlockBooleanMap();
        }
    }

    public ScannerGlassesItem(Holder<ArmorMaterial> p_323783_, Type p_266831_, Properties p_40388_) {
        super(p_323783_, p_266831_, p_40388_);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        initClient();
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public Set<Block> getBlocksToHighlight() {
        initClient();
        Set<Block> blockSet = new HashSet<>();
        for(Block block : blocksToHighlight.keySet()) {
            if(blocksToHighlight.get(block)) {
                blockSet.add(block);
            }
        }
        return blockSet;
    }

    @OnlyIn(Dist.CLIENT)
    public void addBlockToHighlight(Block block) {
        blocksToHighlight.put(block, true);
    }

    @OnlyIn(Dist.CLIENT)
    public void removeBlockToHighlight(Block block) {
        blocksToHighlight.remove(block);
    }

    @OnlyIn(Dist.CLIENT)
    public void clearBlocksToHighlight() {
        blocksToHighlight.clear();
    }
}
