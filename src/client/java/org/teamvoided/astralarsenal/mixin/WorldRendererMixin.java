package org.teamvoided.astralarsenal.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

import static org.teamvoided.astralarsenal.utils.OutlineHelperKt.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private static void drawShapeOutline(MatrixStack matrices, VertexConsumer consumer, VoxelShape voxelShape, double x, double y, double z, float red, float green, float blue, float alpha) {
    }

    @Shadow
    @Final
    private Long2ObjectMap<SortedSet<BlockBreakingInfo>> blockBreakingProgressions;

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void astralArsenal$hammerOutline(MatrixStack matrices, VertexConsumer consumer, Entity entity, double offsetX, double offsetY, double offsetZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        var shapesAndPos = getOutlineShape(client);
        if (shapesAndPos == null) return;

        var pos = shapesAndPos.getSecond();
        drawShapeOutline(
                matrices, consumer, shapesAndPos.getFirst(),
                pos.getX() - offsetX, pos.getY() - offsetY, pos.getZ() - offsetZ,
                0.0F, 0.0F, 0.0F, 0.4F
        );
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getModelViewStack()Lorg/joml/Matrix4fStack;", remap = false))
    private void modifyBreakingInfoStack(DeltaTracker tracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        var positions = getPositions(client);
        if (positions.size() < 2) return;
        addExtraBreakingInfo(client, positions, this.blockBreakingProgressions);
    }
}
