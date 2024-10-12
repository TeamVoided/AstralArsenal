package org.teamvoided.astralarsenal.entity

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

/*
class NailEntityRenderer : EntityModel<Entity?>() {
    private var bb_main: ModelPart? = null
    fun Nail(root: ModelPart) {
        this.bb_main = root.getChild("bb_main")
    }

    override fun setAngles(
        entity: Entity?,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha)
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData: ModelData = ModelData()
                val modelPartData: ModelPartData = modelData.getRoot()
                val bb_main: ModelPartData = modelPartData.addChild(
                    "bb_main",
                    ModelPartBuilder.create().uv(-3, 0).cuboid(-1.5f, 0.0f, -2.0f, 3.0f, 0.0f, 3.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                val cube_r1: ModelPartData = bb_main.addChild(
                    "cube_r1",
                    ModelPartBuilder.create().uv(0, -3).cuboid(1.0f, -1.0f, -1.5f, 0.0f, 3.0f, 3.0f, Dilation(0.0f)),
                    ModelTransform.of(-1.0f, 0.0f, 0.0f, -1.5708f, 0.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 6, 3)
            }
    }
}
*/
