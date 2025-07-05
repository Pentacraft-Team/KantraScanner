package scanner.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.config.ConfigManager;

import java.util.HashSet;
import java.util.Set;

public class BlockScannerItem extends Item {
    @OnlyIn(Dist.CLIENT)
    private Map<Block, Boolean> blocksToHighlight;

    public BlockScannerItem(Properties properties) {
            super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    private void initClient() {
        if (blocksToHighlight == null) {
            blocksToHighlight = ConfigManager.getBlockBooleanMap();
        }
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