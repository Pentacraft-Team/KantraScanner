package scanner.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.config.ConfigManager;

import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class BlockSettingsScreen extends Screen {
    private final Block block;
    private int red, green, blue;
    private final BiConsumer<Block, String> onColorChanged;
    private static final int COLOR_BLOCK_SIZE = 32;
    private static final int COLOR_BLOCK_OUTLINE = 2;
    private static final int TEXT_OFFSET = 5;
    private final Runnable onClose;

    public BlockSettingsScreen(Block block, BlockSelectionList.Entry entry, BiConsumer<Block, String> onColorChanged, Runnable onClose) {
        super(Component.literal("Выбрать цвет"));
        this.block = block;
        this.onColorChanged = onColorChanged;
        this.onClose = onClose;

        // Получаем текущий цвет из конфига или используем белый по умолчанию
        String colorHex = ScannerMenuScreen.blocksColor.getOrDefault(block, "FFFFFF");
        try {
            int color = (int) Long.parseLong(colorHex, 16);
            this.red = (color >> 16) & 0xFF;
            this.green = (color >> 8) & 0xFF;
            this.blue = color & 0xFF;
        } catch (NumberFormatException e) {
            this.red = this.green = this.blue = 255;
        }
    }

    @Override
    protected void init() {
        super.init();

        // Кнопка назад
        this.addRenderableWidget(Button.builder(Component.literal("Сохранить"), button -> {
                    onClose();
            onClose.run();
                })
                .bounds(this.width / 2 - 100, this.height - 30, 200, 20)
                .build());


        int sliderWidth = 200;
        int sliderX = this.width / 2 - sliderWidth / 2;

        AbstractWidget redSlider = new AbstractSliderButton(sliderX, height / 2 - 60, sliderWidth, 20,
                Component.literal("Red: " + red), red / 255.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Red: " + red));
            }

            @Override
            protected void applyValue() {
                red = (int) (this.value * 255);
                updateColor();
            }
        };

        AbstractWidget greenSlider = new AbstractSliderButton(sliderX, height / 2 - 30, sliderWidth, 20,
                Component.literal("Green: " + green), green / 255.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Green: " + green));
            }

            @Override
            protected void applyValue() {
                green = (int) (this.value * 255);
                updateColor();
            }
        };

        AbstractWidget blueSlider = new AbstractSliderButton(sliderX, height / 2, sliderWidth, 20,
                Component.literal("Blue: " + blue), blue / 255.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Blue: " + blue));
            }

            @Override
            protected void applyValue() {
                blue = (int) (this.value * 255);
                updateColor();
            }
        };

        this.addRenderableWidget(redSlider);
        this.addRenderableWidget(greenSlider);
        this.addRenderableWidget(blueSlider);
    }

    private void updateColor() {
        String newColor = String.format("%02x%02x%02x", red, green, blue);
        onColorChanged.accept(block, newColor);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        float scale = 2.5f;
        int iconX = width / 2 - 185;
        int iconY = height / 2 - 50;
        int iconSize = (int)(16 * scale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(iconX, iconY, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.renderItem(new ItemStack(block.asItem()), 0, 0);
        guiGraphics.pose().popPose();

        String text = block.getName().getString();
        int maxTextWidth = (int) (iconSize * 2.5);
        int textWidth = font.width(text);

        float scaleFactor = 1.0f;
        if (textWidth > maxTextWidth) {
            scaleFactor = (float) maxTextWidth / textWidth;
        }

        int textX = iconX + (iconSize / 2);
        int textY = iconY + iconSize  + 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(textX, textY, 0);
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 1.0f);

        guiGraphics.drawString(font, text, (int)(-textWidth / 2f), 0, 0xFFFFFF, false);
        guiGraphics.pose().popPose();


        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();


        int blockX = width / 2 + 150;
        int blockY = height / 2 - 50;


        renderColorBlock(guiGraphics, blockX, blockY);


        String hexColor = String.format("#%02X%02X%02X", red, green, blue);
        guiGraphics.drawString(
                font,
                hexColor,
                blockX + COLOR_BLOCK_SIZE / 2 - font.width(hexColor) / 2,
                blockY + COLOR_BLOCK_SIZE + TEXT_OFFSET,
                0xFFFFFF,
                false
        );


        RenderSystem.enableDepthTest();
    }

    private void renderColorBlock(GuiGraphics guiGraphics, int x, int y) {

        guiGraphics.fill(
                x - COLOR_BLOCK_OUTLINE,
                y - COLOR_BLOCK_OUTLINE,
                x + COLOR_BLOCK_SIZE + COLOR_BLOCK_OUTLINE,
                y + COLOR_BLOCK_SIZE + COLOR_BLOCK_OUTLINE,
                0xFF000000
        );


        int color = 0xFF000000 | (red << 16) | (green << 8) | blue;
        guiGraphics.fill(
                x,
                y,
                x + COLOR_BLOCK_SIZE,
                y + COLOR_BLOCK_SIZE,
                color
        );


        guiGraphics.fill(
                x,
                y,
                x + COLOR_BLOCK_SIZE,
                y + 2,
                darkenColor(color, 0.7f)
        );
        guiGraphics.fill(
                x,
                y,
                x + 2,
                y + COLOR_BLOCK_SIZE,
                darkenColor(color, 0.7f)
        );


        guiGraphics.fill(
                x,
                y + COLOR_BLOCK_SIZE - 2,
                x + COLOR_BLOCK_SIZE,
                y + COLOR_BLOCK_SIZE,
                lightenColor(color, 0.3f)
        );
        guiGraphics.fill(
                x + COLOR_BLOCK_SIZE - 2,
                y,
                x + COLOR_BLOCK_SIZE,
                y + COLOR_BLOCK_SIZE,
                lightenColor(color, 0.3f)
        );
    }

    private int darkenColor(int color, float factor) {
        int r = (int)((color >> 16 & 0xFF) * factor);
        int g = (int)((color >> 8 & 0xFF) * factor);
        int b = (int)((color & 0xFF) * factor);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private int lightenColor(int color, float factor) {
        int r = Math.min(255, (int)((color >> 16 & 0xFF) * (1 + factor)));
        int g = Math.min(255, (int)((color >> 8 & 0xFF) * (1 + factor)));
        int b = Math.min(255, (int)((color & 0xFF) * (1 + factor)));
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public void onClose() {

        ConfigManager.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
