package scanner.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.lwjgl.glfw.GLFW;
import scanner.config.ConfigManager;
import scanner.item.BlockScannerItem;
import scanner.item.ScannerGlassesItem;
import scanner.menu.BlockAddScreen;
import scanner.menu.KeyBindings;
import scanner.menu.ScannerMenuScreen;


import java.util.Map;

import static scanner.util.RenderUtils.renderCircularProgressBar;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    private static final Map<Block, Boolean> blocksMap = ConfigManager.getBlockBooleanMap();
    private final Map<Block, String> blocksColor = ConfigManager.getBlockStringMap();

    public static final BlockHighlightHandler highlightHandler = new BlockHighlightHandler();

    private static long holdStartTime = 0;
    private static boolean isHolding = false;
    private static final int BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    private static boolean isRender = false;
    private static int renderTicks = 0;
    private static boolean isGlassesEnabled = false;
    private static int glassesTicks = 20;


    public static void onClientTick(ClientTickEvent.Post event) {
        if (isRender) {
            if (renderTicks <= ConfigManager.getTICKS_TO_RENDER()) {
                renderTicks++;
            } else {
                renderTicks = 0;
                isRender = false;
                highlightHandler.clearAll();
            }
        }
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem scannerGlasses && isGlassesEnabled && ConfigManager.isXrayEnabled()) {
                isRender = false;
                renderTicks = 0;
                if (glassesTicks <= 20) {
                    glassesTicks++;
                }
                else {
                    glassesTicks = 0;
                    highlightHandler.updateBlocksToHighlight(scannerGlasses.getBlocksToHighlight());
                }
            } else if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem && !isGlassesEnabled && !isRender) {
                highlightHandler.clearAll();
            }
             else if (!(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem) && isGlassesEnabled) {
                 isGlassesEnabled = false;
                highlightHandler.clearAll();
            }
        }
    }

    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        highlightHandler.onBlockDestroyed((Level) event.getLevel(), event.getPos());
    }

    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            highlightHandler.render(
                    event.getPoseStack(),
                    Minecraft.getInstance().renderBuffers().bufferSource(),
                    event.getCamera()
            );
        }
    }


    public static void onMousePress(InputEvent.MouseButton.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!(player.getMainHandItem().getItem() instanceof BlockScannerItem)) return;
        if (event.getButton() == BUTTON && event.getAction() == GLFW.GLFW_PRESS) {
            holdStartTime = System.currentTimeMillis();
            isHolding = true;
        }
    }

    public static void onMouseRelease(InputEvent.MouseButton.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!(player.getMainHandItem().getItem() instanceof BlockScannerItem)) return;
        if (event.getButton() == BUTTON && event.getAction() == GLFW.GLFW_RELEASE) {
            isHolding = false;
        }
    }


    public static void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
        if (!isHolding) return;
        if (!ConfigManager.isXrayEnabled()) return;

        Minecraft minecraft = Minecraft.getInstance();
        int centerX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int centerY = minecraft.getWindow().getGuiScaledHeight() / 2;
        float progress = Math.min(1.0f, (System.currentTimeMillis() - holdStartTime) / (float) ConfigManager.getHOLD_DURATION_MS());

        if (progress >= 1.0f) {
            isHolding = false;
            Player player = minecraft.player;
            if (player != null) {
                ItemStack heldItem = player.getMainHandItem();
                if (heldItem.getItem() instanceof BlockScannerItem scanner && !isGlassesEnabled) {

                    highlightHandler.updateBlocksToHighlight(scanner.getBlocksToHighlight());
                    renderTicks = 0;
                    isRender = true;
                }
            }
            return;
        }

        renderCircularProgressBar(event.getGuiGraphics(), centerX, centerY, progress);
    }

    public static void onRenderGuiX(RenderGuiLayerEvent.Post event) {
        if (isGlassesEnabled) {

            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player == null) return;

            ItemStack RENDER_ITEM = player.getItemBySlot(EquipmentSlot.HEAD);

            GuiGraphics guiGraphics = event.getGuiGraphics();
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();


            int hotbarWidth = 182;
            int hotbarX = (screenWidth - hotbarWidth) / 2;
            int hotbarY = screenHeight - 22;


            int renderX = hotbarX - 30;
            int renderY = hotbarY - 2;
            int size = 24;


            float time = (System.currentTimeMillis() % 2000) / 2000f;
            float bounceOffset = Mth.sin(time * (float)Math.PI * 2) * 3f;


            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(renderX, renderY + bounceOffset, 0);
            guiGraphics.pose().scale(size / 16f, size / 16f, 1);
            guiGraphics.renderItem(RENDER_ITEM, 0, 0);
            guiGraphics.pose().popPose();
        }
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ConfigManager.load();
    }

    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        ConfigManager.save();
    }

    public static void onClientClose(PlayerEvent.PlayerLoggedOutEvent event) {
        ConfigManager.save();
    }

    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        KeyBindings.register(event);
    }

    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Player player = mc.player;
        if (player == null) return;
        if (!(player.getMainHandItem().getItem() instanceof BlockScannerItem || player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem)) return;

        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem) {
            if (KeyBindings.TOGGLE_GLASSES_KEY.consumeClick()) {
                if (ConfigManager.isXrayEnabled())  {
                    isGlassesEnabled = !isGlassesEnabled;
                    glassesTicks = 20;
                }
            }
        }

        if (KeyBindings.OPEN_MENU_KEY.consumeClick()) {
            mc.setScreen(new ScannerMenuScreen());
        }
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (level.isClientSide && player.isShiftKeyDown()) {
            ItemStack held = player.getMainHandItem();

            if (held.getItem() instanceof BlockScannerItem || player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScannerGlassesItem) {
                BlockPos pos = event.getPos();
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                Minecraft.getInstance().setScreen(new BlockAddScreen(block));
                event.setCanceled(true);
            }
        }
    }
}

