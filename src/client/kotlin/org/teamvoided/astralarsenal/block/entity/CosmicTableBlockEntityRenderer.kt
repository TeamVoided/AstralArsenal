package org.teamvoided.astralarsenal.block.entity

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Axis
import org.teamvoided.astralarsenal.block.enity.CosmicTableBlockEntity
import org.teamvoided.astralarsenal.init.AstralItems.KOSMIC_GEM
import kotlin.math.sin

class CosmicTableBlockEntityRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<CosmicTableBlockEntity> {
    val SPEED = 200
    val HALF_SPEED = SPEED / 2
    var itemRenderer: ItemRenderer? = null

    init {
        this.itemRenderer = context.itemRenderer
    }

    override fun render(
        entity: CosmicTableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    ) {
        matrices?.push()

        val world = entity.world!!
        var time = (world.time % SPEED).toFloat()
        time += tickDelta
        time -= HALF_SPEED
        time /= HALF_SPEED
        var bob = (Math.PI * 2 * time).toFloat()
        bob = sin(bob) + 1f
        bob /= 2

        matrices?.translate(0.5, 1.0 + (bob / 4), 0.5)
        matrices?.scale(0.5f, 0.5f, 0.5f)
        matrices?.rotate(Axis.Y_NEGATIVE.rotation((time * 2 * Math.PI).toFloat()))
        itemRenderer!!.renderItem(
            ItemStack(KOSMIC_GEM),
            ModelTransformationMode.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumers,
            entity.world,
            0
        )

        matrices?.pop()
    }
}