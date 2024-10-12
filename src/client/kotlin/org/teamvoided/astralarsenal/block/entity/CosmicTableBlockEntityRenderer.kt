package org.teamvoided.astralarsenal.block.entity

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Axis
import kotlin.math.sin

class CosmicTableBlockEntityRenderer(context: BlockEntityRendererFactory.Context) :
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
        matrices.push()
        val world = block.world ?: error("World is null in CosmicTableBlockEntityRenderer")
        var time = ((world.time + block.pos.asLong()) % SPEED).toFloat()
        time += tickDelta
        time -= HALF_SPEED
        time /= HALF_SPEED
        var bob = sin(Math.PI * 2 * time) + 1f
        bob /= 2
        val inventory = block.getInventory()

        if (!inventory[1].isEmpty) {
            matrices.translate(0.5, 1.0 + (bob / 4), 0.5)
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

        matrices.push()
        matrices.translate(0.5, (12.0 / 16) + 0.03, 0.5)
        matrices.scale(0.75f, 0.75f, 0.75f)

        var h: Float = block.targetItemRotation - block.itemRotation
        while (h >= Math.PI.toFloat()) h -= (Math.PI * 2).toFloat()
        while (h < -Math.PI.toFloat())h += (Math.PI * 2).toFloat()
        val k: Float = block.itemRotation + h * tickDelta
        matrices.rotate(Axis.Y_POSITIVE.rotation(-k))
        matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90f))
        itemRenderer.renderItem(
            inventory[0],
            ModelTransformationMode.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumers,
            block.world,
            0
        )

        matrices.pop()
    }
}