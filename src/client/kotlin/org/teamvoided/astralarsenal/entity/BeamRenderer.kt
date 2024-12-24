package org.teamvoided.astralarsenal.entity

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Color
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import org.teamvoided.astralarsenal.util.toVec3d
import kotlin.math.acos
import kotlin.math.atan2

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
        val cubes = entity.dataTracker.get(BeamRenderEntity.InnerCubes)
        val colour : Int = entity.dataTracker.get(BeamRenderEntity.OuterColour) + (0x01000000 * (255f / (cubes * 3f)).toInt())
        val thickness = entity.dataTracker.get(BeamRenderEntity.OuterThickness)
        val innerColour: Int = entity.dataTracker.get(BeamRenderEntity.InterColour)

        renderCube(entity, matrices, vertexConsumers, thickness, colour)

        for(i in 1..cubes){
            val tempColour = (if(i > (cubes/2)) colour else innerColour) + (0x01000000 * (255 / ((cubes + 1))))
            val tempThickness = (thickness * (i.toFloat()/cubes + 1))
            renderCube(entity, matrices, vertexConsumers, tempThickness, tempColour)
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    fun renderCube(entity: BeamRenderEntity,
                   matrices: MatrixStack,
                   vertexConsumers: VertexConsumerProvider,
                   thickness: Float,
                   colour: Int
    ){
        matrices.push()
        RenderSystem.disableCull()
        val pos = entity.pos
        val targetPos = entity.dataTracker.get(BeamRenderEntity.TargetPos).toVec3d()
        val distance = pos.distanceTo(targetPos)
        matrices.translate(-(thickness*0.5), 0.0, -(thickness*0.5))

        val vec3d: Vec3d = targetPos
        val vec3d2: Vec3d = pos
        var vec3d3 = vec3d.subtract(vec3d2)
        vec3d3 = vec3d3.normalize()
        val n = acos(vec3d3.y).toFloat()
        val o = atan2(vec3d3.z, vec3d3.x).toFloat()
        matrices.rotateAround(Axis.Y_POSITIVE.rotationDegrees(((Math.PI.toFloat() / 2f) - o) * (180f / Math.PI.toFloat())), (thickness * 0.5f), 0f, (thickness * 0.5f))
        matrices.rotateAround(Axis.X_POSITIVE.rotationDegrees(n * (180f / Math.PI.toFloat())), (thickness * 0.5f), 0f, (thickness * 0.5f))

        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning())
        
        val modifiedDistance = distance.toFloat() + 1f

        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,thickness)).color(colour)

        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,thickness)).color(colour)

        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,thickness)).color(colour)

        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,0f)).color(colour)

        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,0f)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,0f)).color(colour)

        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,modifiedDistance,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(0f,0f,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,0f,thickness)).color(colour)
        vertexConsumer.xyz(matrices.peek(), Vector3f(thickness,modifiedDistance,thickness)).color(colour)

        matrices.pop()
    }

    override fun shouldRender(entity: BeamRenderEntity?, frustum: Frustum?, x: Double, y: Double, z: Double): Boolean {
        return true
    }


    override fun getTexture(entity: BeamRenderEntity): Identifier {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
    }

}