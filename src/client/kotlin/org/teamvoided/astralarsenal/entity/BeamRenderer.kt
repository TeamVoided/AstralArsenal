package org.teamvoided.astralarsenal.entity

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import org.joml.Quaternionf
import org.joml.Vector2d
import org.teamvoided.astralarsenal.util.toVec3d
import kotlin.math.absoluteValue
import kotlin.math.asin

class BeamRenderer(context: EntityRendererFactory.Context?) :
    EntityRenderer<BeamRenderEntity>(context) {

    override fun render(
        entity: BeamRenderEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()
        val thickness = entity.dataTracker.get(BeamRenderEntity.OuterThickness)
        val pos = entity.pos
        val targetPos = entity.dataTracker.get(BeamRenderEntity.TargetPos).toVec3d()
        val distance = pos.distanceTo(targetPos)
        matrices.scale(thickness, distance.toFloat(), thickness)
        matrices.rotateAround(Quaternionf(), (thickness * 0.5f), 0f, (thickness * 0.5f))
        matrices.translate(entity.x, entity.y, entity.z)

        val posXY = Vector2d(entity.x, entity.y)
        val tposXY = Vector2d(targetPos.x, targetPos.y)
        val deltaX = (posXY.x - tposXY.x).absoluteValue
        val deltaY = (posXY.y - tposXY.y).absoluteValue
        val distanceXY = posXY.distance(tposXY)
        val XYRotation = when{
            (tposXY.x < posXY.x && tposXY.y > posXY.y) -> asin((deltaX)/distanceXY).toFloat()
            (tposXY.x < posXY.x && tposXY.y < posXY.y) -> 90f + asin((deltaY)/distanceXY).toFloat()
            (tposXY.x > posXY.x && tposXY.y < posXY.y) -> 180f + asin(deltaX/distanceXY).toFloat()
            (tposXY.x > posXY.x && tposXY.y > posXY.y) -> 270f + asin(deltaY/distanceXY).toFloat()
            else -> 0f
        }
        matrices.rotate(Axis.Z_NEGATIVE.rotationDegrees(XYRotation))

        val posXZ = Vector2d(entity.x, entity.z)
        val tposXZ = Vector2d(targetPos.x, targetPos.z)
        //don't be stupid! .y here is .z for 2d vectors here.(this should have been done with the 3d vectors)
        val deltaZ = (posXZ.y - tposXZ.y).absoluteValue
        val distanceXZ = posXZ.distance(tposXZ)
        val XZRotation = when{
            (XYRotation > 180f && tposXZ.x > posXZ.x) -> 270f + asin(deltaX/distanceXZ).toFloat()
            (XYRotation > 180f && tposXZ.x < posXZ.x) -> asin(deltaZ/distanceXZ).toFloat()
            (XYRotation < 180f && tposXZ.x > posXZ.x) -> asin(deltaX/distanceXZ).toFloat()
            (XYRotation < 180f && tposXZ.x < posXZ.x) -> 270f + asin(deltaZ/distanceXZ).toFloat()
            else -> 0f
        }
        matrices.rotate(Axis.Y_NEGATIVE.rotationDegrees(XZRotation))

        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning())

        vertexConsumer.xyz(matrices.peek(), pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat()).color(entity.dataTracker.get(BeamRenderEntity.OuterColour))
        matrices.pop()

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }


    override fun getTexture(entity: BeamRenderEntity): Identifier {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
    }

}