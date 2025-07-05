package scanner.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import scanner.config.ConfigManager;
import scanner.util.RenderUtils;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class BlockHighlightHandler {
    private final Set<Block> trackedBlocks = new HashSet<>();
    private final Map<BlockPos, Block> foundBlocks = new HashMap<>();
    private final Map<Block, String> blocksColors = ConfigManager.getBlockStringMap();
    private final WeakHashMap<Level, Set<BlockPos>> brokenBlocks = new WeakHashMap<>();

    public void updateBlocksToHighlight(Set<Block> blocks) {
        trackedBlocks.clear();
        trackedBlocks.addAll(blocks);
        rescanArea();
    }

    private void rescanArea() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        foundBlocks.clear();
        BlockPos center = mc.player.blockPosition();
        int radius = ConfigManager.getXrayRange();

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {

            double dx = pos.getX() - center.getX();
            double dy = pos.getY() - center.getY();
            double dz = pos.getZ() - center.getZ();

            double distanceSq = dx * dx + dy * dy + dz * dz;

            if (distanceSq <= radius * radius) {
                BlockState state = mc.level.getBlockState(pos);
                if (trackedBlocks.contains(state.getBlock())) {
                    foundBlocks.put(pos.immutable(), state.getBlock());
                }
            }
        }

        Set<BlockPos> broken = brokenBlocks.getOrDefault(mc.level, Collections.emptySet());
        broken.forEach(foundBlocks::remove);
    }

    public void onBlockDestroyed(Level level, BlockPos pos) {
        if (foundBlocks.containsKey(pos)) {
            brokenBlocks.computeIfAbsent(level, k -> new HashSet<>()).add(pos);
            foundBlocks.remove(pos);
        }
    }

    public void clearAll() {
        foundBlocks.clear();
        brokenBlocks.clear();
    }

//    public void render(PoseStack poseStack, MultiBufferSource buffer, Camera camera) {
//        if (foundBlocks.isEmpty()) return;
//
//        Vec3 camPos = camera.getPosition();
//        poseStack.pushPose();
//        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
//
//        Map<BlockPos, Block> copy = new HashMap<>(foundBlocks);
//
//        for (Map.Entry<BlockPos, Block> entry : copy.entrySet()) {
//            String color = blocksColors.get(entry.getValue());
//            RenderUtils.renderXRaySurfaces(poseStack, buffer, new AABB(entry.getKey()), Integer.parseInt(color, 16));
//        }
//
//        poseStack.popPose();
//    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, Camera camera) {
        if (foundBlocks.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        Vec3 camPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        Map<BlockPos, Block> copy = new HashMap<>(foundBlocks);

        for (Map.Entry<BlockPos, Block> entry : copy.entrySet()) {
            BlockPos pos = entry.getKey();
            Block block = entry.getValue();
            String color = blocksColors.get(block);
            int hexColor = Integer.parseInt(color, 16);

            // Проверяем, какие грани рендерить
            boolean[] renderSides = new boolean[6]; // порядок: DOWN, UP, NORTH, SOUTH, WEST, EAST
            renderSides[0] = shouldRenderSide(level, pos, Direction.DOWN, block);
            renderSides[1] = shouldRenderSide(level, pos, Direction.UP, block);
            renderSides[2] = shouldRenderSide(level, pos, Direction.NORTH, block);
            renderSides[3] = shouldRenderSide(level, pos, Direction.SOUTH, block);
            renderSides[4] = shouldRenderSide(level, pos, Direction.WEST, block);
            renderSides[5] = shouldRenderSide(level, pos, Direction.EAST, block);

            RenderUtils.renderXRaySurfacesSelective(poseStack, buffer, new AABB(pos), hexColor, renderSides);
        }

        poseStack.popPose();
    }

    private boolean shouldRenderSide(Level level, BlockPos pos, Direction side, Block block) {
        BlockPos neighborPos = pos.relative(side);
        BlockState neighborState = level.getBlockState(neighborPos);
        return neighborState.getBlock() != block;
    }
}
