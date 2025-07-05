package scanner.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.config.ConfigManager;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ScannerMenuScreen extends Screen {
    private BlockSelectionList blockList;
    private static final Map<Block, Boolean> blocksMap = ConfigManager.getBlockBooleanMap();
    static final Map<Block, String> blocksColor = ConfigManager.getBlockStringMap();

    public ScannerMenuScreen() {
        super(Component.literal("Block Selection"));
    }

    @Override
    protected void init() {
        this.blockList = new BlockSelectionList(
                minecraft,
                this.width,
                this.height - 60,
                30,
                block -> {
                    blocksMap.remove(block);
                    blocksColor.remove(block);
                },
                this::saveChanges,
                this::openBlockSettings
        );

        blocksMap.forEach((block, enabled) ->
                blockList.addBlockEntry(block, enabled, blocksColor.get(block))
        );

        this.addRenderableWidget(blockList);

        this.addRenderableWidget(
                Button.builder(Component.literal("Закрыть"), button -> onClose())
                        .bounds(this.width / 2 - 100, this.height - 26, 200, 20)
                        .build()
        );
    }

    private void openBlockSettings(Block block, BlockSelectionList.Entry entry) {
        minecraft.setScreen(new BlockSettingsScreen(block, entry, this::updateColorForEntry, () -> minecraft.setScreen(this)));
    }

    private void updateColorForEntry(Block block, String newColor) {
        blocksColor.put(block, newColor);
    }

    private void saveChanges() {
        blockList.children().forEach(entry ->
                blocksMap.put(entry.getBlock(), entry.isEnabled())
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        blockList.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
