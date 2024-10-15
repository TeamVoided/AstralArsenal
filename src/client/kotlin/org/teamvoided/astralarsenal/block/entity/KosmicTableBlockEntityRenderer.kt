package org.teamvoided.astralarsenal.block.entity

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Axis
import org.teamvoided.astralarsenal.data.tags.AstralItemTags.KOSMIC_TABLE_LEFT_FACING
import org.teamvoided.astralarsenal.data.tags.AstralItemTags.KOSMIC_TABLE_STRAIGHT
import kotlin.math.sin

class KosmicTableBlockEntityRenderer(context: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<CosmicTableBlockEntity> {
    private val SPEED = 200
    private val HALF_SPEED = SPEED / 2
    private var itemRenderer: ItemRenderer = context.itemRenderer

    override fun render(
        block: CosmicTableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val inventory = block.getInventory()
        val tool = inventory[0]
        matrices.push()
        val world = block.world ?: error("World is null in CosmicTableBlockEntityRenderer")
        var time = ((world.time + block.pos.asLong()) % SPEED).toFloat()
        time += tickDelta
        time -= HALF_SPEED
        time /= HALF_SPEED
        var bob = sin(Math.PI * 2 * time) + 1f
        bob /= 2

        if (!inventory[1].isEmpty) {
            matrices.translate(0.5, (if (tool.isEmpty) 1.0 else 1.1) + (bob / 4), 0.5)
            matrices.scale(0.5f, 0.5f, 0.5f)
            matrices.rotate(Axis.Y_NEGATIVE.rotation((time * 2 * Math.PI).toFloat()))
            itemRenderer.renderItem(
                block.getInventory()[1],
                ModelTransformationMode.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                block.world,
                0
            )
        }
        matrices.pop()
        if (!tool.isEmpty) {
            matrices.push()
            matrices.translate(0.5, (12.0 / 16) + 0.03, 0.5)
            matrices.scale(0.75f, 0.75f, 0.75f)

            var h: Float = block.targetItemRotation - block.itemRotation
            val pi = Math.PI.toFloat()
            while (h >= pi) h -= (pi * 2f)
            while (h < -pi) h += (pi * 2f)
            val k = block.itemRotation + h * (tickDelta / 2)
            matrices.rotate(Axis.Y_POSITIVE.rotation(-k - getAdditionalAngle(tool).rad.toFloat()))
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90f))
            itemRenderer.renderItem(
                inventory[0], ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, block.world, 0
            )
            matrices.pop()
        }
    }

    private fun getAdditionalAngle(stack: ItemStack) =
        if (stack.isIn(KOSMIC_TABLE_LEFT_FACING)) 135
        else if (stack.isIn(KOSMIC_TABLE_STRAIGHT)) 90
        else 45

    val Number.rad get() = Math.toRadians(this.toDouble())
}