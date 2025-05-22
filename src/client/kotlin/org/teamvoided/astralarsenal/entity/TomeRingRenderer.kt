package org.teamvoided.astralarsenal.entity

import net.minecraft.client.model.Model
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.effects.AstralStatusEffect

class TomeRingRenderer<T : LivingEntity, V : EntityModel<T>>(
    context: LivingEntityRenderer<T, V>?,
    factory: EntityRendererFactory.Context
) :
    FeatureRenderer<T, V>(
        context
    ) {

    val model: Model = TomeRingModel(factory.getPart(TomeRingModel.MODEL_LAYER))

    override fun render(
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        entity: T?,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val tomeEffects = entity!!.statusEffects.filter { instance ->
            val effect = instance.effectType.value()
            return@filter effect is AstralStatusEffect && effect.showTomeRings
        }

        if (tomeEffects.isNotEmpty()) {
            val consumer = vertexConsumers?.getBuffer(RenderLayer.getEntityCutout(TEXTURE))
            val age = entity.age + tickDelta

            matrices?.rotate(Axis.Y_POSITIVE.rotation(age / 20 + MathHelper.PI))
            matrices?.translate(0F, MathHelper.sin(age / 10 + MathHelper.PI) * 0.1F + 0.1F, 0F)

            model.method_2828(
                matrices, consumer,
                LightmapTextureManager.pack(15, 15),
                LivingEntityRenderer.getOverlay(entity, 0F),
                tomeEffects.first().effectType?.value()?.color ?: -1
            )
        }
    }

    companion object {
        val TEXTURE = AstralArsenal.id("textures/entity/tome_ring.png")
    }
}
