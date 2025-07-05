package scanner.menu;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class BlockSelectionList extends AbstractSelectionList<BlockSelectionList.Entry> {
    private final int listWidth;
    private static final int ITEM_HEIGHT = 20;
    private final Consumer<Block> onRemoveCallback;
    private final Runnable onChangeCallback;
    private final BiConsumer<Block, Entry> onSettingsCallback;

    public BlockSelectionList(Minecraft minecraft, int width, int height, int y0, Consumer<Block> onRemoveCallback, Runnable onChangeCallback, BiConsumer<Block, Entry> onSettingsCallback) {
        super(minecraft, width, height, y0, ITEM_HEIGHT);
        this.listWidth = width;
        this.onRemoveCallback = onRemoveCallback;
        this.onChangeCallback = onChangeCallback;
        this.onSettingsCallback = onSettingsCallback;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.listWidth - 6;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth - 20;
    }

    public void addBlockEntry(Block block, boolean enabled, String hexColor) {
        super.addEntry(new Entry(block, enabled, this.minecraft, b -> {
            this.onRemoveCallback.accept(b);
            this.minecraft.screen.init(minecraft, minecraft.screen.width, minecraft.screen.height);
        }, onChangeCallback, hexColor, (b, e) -> onSettingsCallback.accept(b, e)));
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE,
                Component.translatable("narration.block_selection.list"));
    }

    public static class Entry extends AbstractSelectionList.Entry<Entry> {
        @Getter
        private final Block block;
        @Getter
        private boolean enabled;
        private final Checkbox checkbox;
        private final Button trashButton;
        private final Minecraft minecraft;
        private static final int PADDING = 2;
        private final Runnable onCheckboxChanged;
        private final Button openScreenButton;
        private final String colorHex;
        private final BiConsumer<Block, Entry> onSettingsCallback;

        public Entry(Block block, boolean enabled, Minecraft minecraft, Consumer<Block> onRemove, Runnable onCheckboxChanged, String colorHex, BiConsumer<Block, Entry> onSettingsCallback) {
            this.block = block;
            this.enabled = enabled;
            this.minecraft = minecraft;
            this.onCheckboxChanged = onCheckboxChanged;
            this.colorHex = colorHex;
            this.onSettingsCallback = onSettingsCallback;

            this.checkbox = Checkbox.builder(Component.empty(), minecraft.font)
                    .pos(0, 0)
                    .selected(enabled)
                    .build();


            this.trashButton = Button.builder(Component.literal("🗑"), button -> {
                        onRemove.accept(this.block);
                    })
                    .bounds(0, 0, 18, 18)
                    .build();


            this.openScreenButton = Button.builder(Component.literal("⚙"), button -> {
                        onSettingsCallback.accept(this.block, this);
                    })
                    .bounds(0, 0, 18, 18)
                    .build();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left,
                           int width, int height, int mouseX, int mouseY,
                           boolean isMouseOver, float partialTick) {

            int color = (int) Long.parseLong(colorHex, 16) | 0xFF000000; // Convert hex to ARGB
            int circleSize = 8;
            int circleY = top + (height - circleSize) / 2;
            guiGraphics.fill(left + PADDING, circleY,
                    left + PADDING + circleSize,
                    circleY + circleSize,
                    color);


            int iconY = top + (height - 16) / 2;
            guiGraphics.renderItem(new ItemStack(block.asItem()), left + PADDING + 10, iconY);


            int textY = top + (height - minecraft.font.lineHeight) / 2;
            guiGraphics.drawString(
                    minecraft.font,
                    block.getName().getString(),
                    left + 30,
                    textY,
                    0xFFFFFF
            );


            checkbox.setX(left + width - 66);
            checkbox.setY(top + (height - 18) / 2);
            checkbox.render(guiGraphics, mouseX, mouseY, partialTick);

            openScreenButton.setX(left + width - 44);
            openScreenButton.setY(top + (height - 18) / 2);
            openScreenButton.render(guiGraphics, mouseX, mouseY, partialTick);

            trashButton.setX(left + width - 22);
            trashButton.setY(top + (height - 18) / 2);
            trashButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (checkbox.mouseClicked(mouseX, mouseY, button)) {
                this.enabled = checkbox.selected();
                onCheckboxChanged.run();
                return true;
            }
            if (trashButton.mouseClicked(mouseX, mouseY, button)) {
                return trashButton.mouseClicked(mouseX, mouseY, button);
            }

            if (openScreenButton.mouseClicked(mouseX, mouseY, button)) {
                return openScreenButton.mouseClicked(mouseX, mouseY, button);
            }
            return false;
        }

    }
}
