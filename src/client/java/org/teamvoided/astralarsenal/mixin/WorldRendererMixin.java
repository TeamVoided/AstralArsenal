package org.teamvoided.astralarsenal.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.astralarsenal.Utils_HelperKt.getShapeAndPos;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;


    @Shadow
    private static void drawShapeOutline(MatrixStack matrices, VertexConsumer consumer, VoxelShape voxelShape, double x, double y, double z, float red, float green, float blue, float alpha) {
    }

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void astralArsenal$hammerOutline(MatrixStack matrices, VertexConsumer consumer, Entity entity, double offsetX, double offsetY, double offsetZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        var shapesAndPos = getShapeAndPos(client);
        if (shapesAndPos == null) return;

        var pos = shapesAndPos.getSecond();
        shapesAndPos.getFirst().forEach(shape -> {
            drawShapeOutline(
                    matrices,
                    consumer,
                    shape,
                    (double) pos.getX() - offsetX,
                    (double) pos.getY() - offsetY,
                    (double) pos.getZ() - offsetZ,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.4F);
        });
        ci.cancel();
    }

}
