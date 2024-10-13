package org.teamvoided.astralarsenal.entity

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.entity.nails.NailEntity

class NailEntityRenderer<T : NailEntity>(context: EntityRendererFactory.Context?) :
    EntityRenderer<T>(context) {
    override fun render(
        persistentProjectileEntity: T, f: Float, g: Float, matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider, i: Int
    ) {
        matrices.push()
        matrices.rotate(
            Axis.Y_POSITIVE.rotationDegrees(
                MathHelper.lerp(
                    g,
                    persistentProjectileEntity.prevYaw,
                    persistentProjectileEntity.yaw
                ) - 90.0f
            )
        )
        matrices.rotate(
            Axis.Z_POSITIVE.rotationDegrees(
                MathHelper.lerp(
                    g,
                    persistentProjectileEntity.prevPitch,
                    persistentProjectileEntity.pitch
                )
            )
        )
        matrices.rotate(Axis.X_POSITIVE.rotationDegrees(45.0f))
        matrices.scale(0.05625f, 0.05625f, 0.05625f)
        val vertexConsumer =
            vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(persistentProjectileEntity)))
        val entry = matrices.peek()

        for (u in 0..3) {
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90.0f))
            vertex(entry, vertexConsumer, -2, -2, 0, 0.375f, 0.0f, 0, 1, 0, i)
            vertex(entry, vertexConsumer, 1, -2, 0, 0.375f, 0.375f, 0, 1, 0, i)
            vertex(entry, vertexConsumer, 1, 2, 0, 0.0f, 0.375f, 0, 1, 0, i)
            vertex(entry, vertexConsumer, -2, 2, 0, 0.0f, 0.0f, 0, 1, 0, i)
        }

        matrices.rotate(Axis.X_POSITIVE.rotationDegrees(45.0f))
        matrices.translate(0f, -0.5f, -0.5f)
        this.vertex(entry, vertexConsumer, -2, -1, -1, 0.375f, 0.0f, -1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, -1, 2, 0.75f, 0.0f, -1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, 2, 2, 0.75f, 0.375f, -1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, 2, -1, 0.375f, 0.375f, -1, 0, 0, i)

        this.vertex(entry, vertexConsumer, -2, 2, -1, 0.375f, 0.0f, 1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, 2, 2, 0.75f, 0.0f, 1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, -1, 2, 0.75f, 0.375f, 1, 0, 0, i)
        this.vertex(entry, vertexConsumer, -2, -1, -1, 0.375f, 0.375f, 1, 0, 0, i)

        matrices.pop()
        super.render(persistentProjectileEntity, f, g, matrices, vertexConsumers, i)
    }

    fun vertex(
        entry: MatrixStack.Entry, vertexConsumer: VertexConsumer, x: Int, y: Int, z: Int, u: Float, v: Float,
        normalX: Int, normalZ: Int, normalY: Int, light: Int
    ) {
        vertexConsumer.xyz(entry, x.toFloat(), y.toFloat(), z.toFloat())
            .color(-1)
            .uv0(u, v)
            .uv1(OverlayTexture.DEFAULT_UV)
            .uv2(light)
            .normal(entry, normalX.toFloat(), normalY.toFloat(), normalZ.toFloat())
    }

    override fun getTexture(entity: T): Identifier = when (entity.nailType) {
        NailEntity.NailType.BASE -> NAIL_TEXTURE
        NailEntity.NailType.FIRE -> FIRE_NAIL_TEXTURE
        NailEntity.NailType.CHARGED -> CHARGED_NAIL_TEXTURE
    }

    companion object {
        val NAIL_TEXTURE = id("textures/entity/projectiles/nail.png")
        val FIRE_NAIL_TEXTURE = id("textures/entity/projectiles/fire_nail.png")
        val CHARGED_NAIL_TEXTURE = id("textures/entity/projectiles/charged_nail.png")
    }
}
