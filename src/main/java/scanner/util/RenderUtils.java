package scanner.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;


import static net.minecraft.client.renderer.RenderStateShard.*;


//  ДАЛЬШЕ БОГА НЕТ
//  ДАЛЬШЕ БОГА НЕТ
//  ДАЛЬШЕ БОГА НЕТ
//  ДАЛЬШЕ БОГА НЕТ
//  ДАЛЬШЕ БОГА НЕТ
//  ДАЛЬШЕ БОГА НЕТ

public class RenderUtils {
//    private static final float LINE_WIDTH = 2.5f;
//    private static final float MARGIN = 0.001f;
//
//    private static final RenderType XRAY_LINES = RenderType.create(
//            "xray_lines",
//            DefaultVertexFormat.POSITION_COLOR,
//            VertexFormat.Mode.DEBUG_LINES,
//            256,
//            false,
//            false,
//            RenderType.CompositeState.builder()
//                    .setShaderState(RENDERTYPE_LINES_SHADER)
//                    .setLineState(new LineStateShard(OptionalDouble.of(LINE_WIDTH)))
//                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
//                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//                    .setDepthTestState(NO_DEPTH_TEST)
//                    .setWriteMaskState(COLOR_WRITE)
//                    .setCullState(NO_CULL)
//                    .createCompositeState(false)
//    );
//
//    public static void renderXRayOutline(PoseStack poseStack, MultiBufferSource buffer, AABB aabb, int hexColor) {
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.depthMask(false);
//        RenderSystem.disableCull();
//
//        AABB renderAABB = aabb.inflate(MARGIN);
//
//        VertexConsumer builder = buffer.getBuffer(XRAY_LINES);
//        Matrix4f matrix = poseStack.last().pose();
//
//        float r = ((hexColor >> 16) & 0xFF) / 255.0f;
//        float g = ((hexColor >> 8) & 0xFF) / 255.0f;
//        float b = (hexColor & 0xFF) / 255.0f;
//        float a = 0.3f;
//
//        renderEdges(builder, matrix, renderAABB, r, g, b, a);
//
//        RenderSystem.depthMask(true);
//        RenderSystem.enableCull();
//        RenderSystem.disableBlend();
//    }
//
//    private static void renderEdges(VertexConsumer builder, Matrix4f matrix, AABB aabb,
//                                    float r, float g, float b, float a) {
//        float minX = (float)aabb.minX;
//        float minY = (float)aabb.minY;
//        float minZ = (float)aabb.minZ;
//        float maxX = (float)aabb.maxX;
//        float maxY = (float)aabb.maxY;
//        float maxZ = (float)aabb.maxZ;
//
//        // Нижние рёбра (Z меняется)
//        line(builder, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
//        line(builder, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
//        line(builder, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
//        line(builder, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);
//
//        // Верхние рёбра (Z меняется)
//        line(builder, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
//        line(builder, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
//        line(builder, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
//        line(builder, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);
//
//        // Вертикальные рёбра (Y меняется)
//        line(builder, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
//        line(builder, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
//        line(builder, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
//        line(builder, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
//    }

    private static final RenderType XRAY_SURFACES = RenderType.create(
            "xray_surfaces",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, // Используем QUADS вместо DEBUG_LINES
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER) // или подходящий для поверхности, если другой шейдер нужен
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false)
    );
//
//    public static void renderXRaySurfaces(PoseStack poseStack, MultiBufferSource buffer, AABB aabb, int hexColor) {
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.depthMask(false);
//        RenderSystem.disableCull();
//
////        AABB renderAABB = aabb.inflate(MARGIN);
//
//        VertexConsumer builder = buffer.getBuffer(XRAY_SURFACES);
//        Matrix4f matrix = poseStack.last().pose();
//
//        float r = ((hexColor >> 16) & 0xFF) / 255.0f;
//        float g = ((hexColor >> 8) & 0xFF) / 255.0f;
//        float b = (hexColor & 0xFF) / 255.0f;
//        float a = 0.1f;
//
//        renderFaces(builder, matrix, aabb, r, g, b, a);
//
//        RenderSystem.depthMask(true);
//        RenderSystem.enableCull();
//        RenderSystem.disableBlend();
//    }
//
//    private static void renderFaces(VertexConsumer builder, Matrix4f matrix, AABB aabb,
//                                    float r, float g, float b, float a) {
//        float x1 = (float)aabb.minX, y1 = (float)aabb.minY, z1 = (float)aabb.minZ;
//        float x2 = (float)aabb.maxX, y2 = (float)aabb.maxY, z2 = (float)aabb.maxZ;
//
//        // Порядок вершин: против часовой стрелки при взгляде снаружи
//
//        // Нижняя грань (Y = min)
//        quad(builder, matrix, x1, y1, z2, x2, y1, z2, x2, y1, z1, x1, y1, z1, r, g, b, a);
//        // Верхняя грань (Y = max)
//        quad(builder, matrix, x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2, r, g, b, a);
//        // Передняя грань (Z = min)
//        quad(builder, matrix, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, r, g, b, a);
//        // Задняя грань (Z = max)
//        quad(builder, matrix, x2, y1, z2, x1, y1, z2, x1, y2, z2, x2, y2, z2, r, g, b, a);
//        // Левая грань (X = min)
//        quad(builder, matrix, x1, y1, z2, x1, y1, z1, x1, y2, z1, x1, y2, z2, r, g, b, a);
//        // Правая грань (X = max)
//        quad(builder, matrix, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, r, g, b, a);
//    }
//
    private static void quad(VertexConsumer builder, Matrix4f matrix,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float x4, float y4, float z4,
                             float r, float g, float b, float a) {
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
        builder.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a);
        builder.addVertex(matrix, x3, y3, z3).setColor(r, g, b, a);
        builder.addVertex(matrix, x4, y4, z4).setColor(r, g, b, a);
    }

    public static void renderCircularProgressBar(GuiGraphics guiGraphics, int centerX, int centerY, float progress) {
        PoseStack poseStack = guiGraphics.pose();
        int segments = 36;
        int outerRadius = 20;
        int innerRadius = 16;

        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i <= segments * progress; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            buffer.addVertex(poseStack.last().pose(), cos * outerRadius, sin * outerRadius, 0)
                    .setColor(0.9f, 0.9f, 0.9f, 0.7f);

            buffer.addVertex(poseStack.last().pose(), cos * innerRadius, sin * innerRadius, 0)
                    .setColor(0.9f, 0.9f, 0.9f, 0.7f);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    private static void line(VertexConsumer builder, Matrix4f matrix,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float r, float g, float b, float a) {
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
        builder.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a);
    }

    public static void renderXRaySurfacesSelective(PoseStack poseStack, MultiBufferSource buffer, AABB aabb, int hexColor, boolean[] renderSides) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.disableColorLogicOp();

        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1, -1);

        VertexConsumer builder = buffer.getBuffer(XRAY_SURFACES);
        Matrix4f matrix = poseStack.last().pose();

        float r = ((hexColor >> 16) & 0xFF) / 255.0f;
        float g = ((hexColor >> 8) & 0xFF) / 255.0f;
        float b = (hexColor & 0xFF) / 255.0f;
        float a = 0.8f; // Прозрачность

        // Рендерим полупрозрачные стороны
        if (renderSides[0]) renderBottomFace(builder, matrix, aabb, r, g, b, a);
        if (renderSides[1]) renderTopFace(builder, matrix, aabb, r, g, b, a);
        if (renderSides[2]) renderNorthFace(builder, matrix, aabb, r, g, b, a);
        if (renderSides[3]) renderSouthFace(builder, matrix, aabb, r, g, b, a);
        if (renderSides[4]) renderWestFace(builder, matrix, aabb, r, g, b, a);
        if (renderSides[5]) renderEastFace(builder, matrix, aabb, r, g, b, a);

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.disablePolygonOffset();
    }

    private static final float MARGIN = 0.00005f;

    private static void renderBottomFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x1 = (float) aabb.minX - MARGIN, y = (float) aabb.minY - MARGIN, z1 = (float) aabb.minZ - MARGIN;
        float x2 = (float) aabb.maxX + MARGIN, z2 = (float) aabb.maxZ + MARGIN;
        quad(builder, matrix, x1, y, z2, x2, y, z2, x2, y, z1, x1, y, z1, r, g, b, a);
    }

    private static void renderTopFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x1 = (float) aabb.minX - MARGIN, y = (float) aabb.maxY + MARGIN, z1 = (float) aabb.minZ - MARGIN;
        float x2 = (float) aabb.maxX + MARGIN, z2 = (float) aabb.maxZ + MARGIN;
        quad(builder, matrix, x1, y, z1, x2, y, z1, x2, y, z2, x1, y, z2, r, g, b, a);
    }

    private static void renderNorthFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x1 = (float) aabb.minX - MARGIN, y1 = (float) aabb.minY - MARGIN, y2 = (float) aabb.maxY + MARGIN, z = (float) aabb.minZ - MARGIN;
        float x2 = (float) aabb.maxX + MARGIN;
        quad(builder, matrix, x1, y1, z, x2, y1, z, x2, y2, z, x1, y2, z, r, g, b, a);
    }

    private static void renderSouthFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x1 = (float) aabb.minX - MARGIN, y1 = (float) aabb.minY - MARGIN, y2 = (float) aabb.maxY + MARGIN, z = (float) aabb.maxZ + MARGIN;
        float x2 = (float) aabb.maxX + MARGIN;
        quad(builder, matrix, x2, y1, z, x1, y1, z, x1, y2, z, x2, y2, z, r, g, b, a);
    }

    private static void renderWestFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x = (float) aabb.minX - MARGIN, y1 = (float) aabb.minY - MARGIN, y2 = (float) aabb.maxY + MARGIN, z1 = (float) aabb.minZ - MARGIN, z2 = (float) aabb.maxZ + MARGIN;
        quad(builder, matrix, x, y1, z2, x, y1, z1, x, y2, z1, x, y2, z2, r, g, b, a);
    }

    private static void renderEastFace(VertexConsumer builder, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float x = (float) aabb.maxX + MARGIN, y1 = (float) aabb.minY - MARGIN, y2 = (float) aabb.maxY + MARGIN, z1 = (float) aabb.minZ - MARGIN, z2 = (float) aabb.maxZ + MARGIN;
        quad(builder, matrix, x, y1, z1, x, y1, z2, x, y2, z2, x, y2, z1, r, g, b, a);
    }
}