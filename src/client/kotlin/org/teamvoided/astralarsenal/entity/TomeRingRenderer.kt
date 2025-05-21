package org.teamvoided.astralarsenal.entity

import net.minecraft.client.model.Model
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.effects.AstralStatusEffect

@Suppress("UNCHECKED_CAST")
class TomeRingRenderer(
    context: LivingEntityRenderer<*, *>?,
    factory: EntityRendererFactory.Context
) :
    FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(
        context as FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
    ) {

    val model: Model = TomeRingModel(factory.getPart(TomeRingModel.MODEL_LAYER))

    override fun render(
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        player: AbstractClientPlayerEntity?,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val tomeEffects = player!!.statusEffects.filter { instance ->
            val effect = instance.effectType.value()
            return@filter effect is AstralStatusEffect && effect.showTomeRings
        }

        if (tomeEffects.isNotEmpty()) {
            val consumer = vertexConsumers?.getBuffer(RenderLayer.getEntityCutout(TEXTURE))
            matrices?.rotate(Axis.Y_POSITIVE.rotation((player.age + tickDelta) / 20 + (MathHelper.PI)))

            model.method_2828(
                matrices, consumer, light,
                LivingEntityRenderer.getOverlay(player, 0F),
                tomeEffects.first().effectType?.value()?.color ?: -1
            )
        }
    }

    companion object {
        val TEXTURE = AstralArsenal.id("textures/entity/tome_ring.png")
    }
}
