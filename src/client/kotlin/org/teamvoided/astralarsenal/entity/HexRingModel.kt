package org.teamvoided.astralarsenal.entity

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.astralarsenal.AstralArsenal

class HexRingModel(val rootPart: ModelPart) : Model(RenderLayer::getEntityCutout) {

    override fun method_2828(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        light: Int,
        overlay: Int,
        color: Int
    ) {
        rootPart.method_22699(matrices, vertexConsumer, light, overlay, color)
    }

    companion object {
        val MODEL_LAYER: EntityModelLayer = EntityModelLayer(AstralArsenal.id("hex_ring"), "main")

        fun createLayer(): TexturedModelData? {
            val modelData = ModelData()
            val partData = modelData.root

            val front: ModelPartData = partData.addChild(
                "front",
                ModelPartBuilder.create().uv(0, 0).cuboid(-9.0f, -20.5f, -9.0f, 18.0f, 5.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            front.addChild(
                "back",
                ModelPartBuilder.create().uv(0, 15).cuboid(-9.0f, -20.5f, -9.0f, 18.0f, 5.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, -1.5708f, 0.0f)
            )

            front.addChild(
                "left",
                ModelPartBuilder.create().uv(0, 10).cuboid(-9.0f, -20.5f, -9.0f, 18.0f, 5.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 3.1416f, 0.0f)
            )

            front.addChild(
                "right",
                ModelPartBuilder.create().uv(0, 5).cuboid(-9.0f, -20.5f, -9.0f, 18.0f, 5.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 1.5708f, 0.0f)
            )

            return TexturedModelData.of(modelData, 48, 32)
        }
    }
}