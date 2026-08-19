package org.teamvoided.astralarsenal.entity

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory.Context
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import org.teamvoided.astralarsenal.utils.CustomUseAnimation.astralBladeModel

class AstralProjectionEntityRenderer(ctx: Context) : EntityRenderer<AstralProjectionEntity>(ctx) {
    var itemRenderer: ItemRenderer = ctx.itemRenderer

    override fun render(
        entity: AstralProjectionEntity,
        yaw: Float, tickDelta: Float, posStack: MatrixStack, consumers: VertexConsumerProvider, light: Int,
    ) {
        posStack.push()
        posStack.translate(0.0, 0.1, 0.0)

        posStack.rotate(Axis.Y_NEGATIVE.rotationDegrees(entity.getYaw(tickDelta) + 90f))
        posStack.rotate(Axis.Z_NEGATIVE.rotationDegrees(entity.getPitch(tickDelta) + 135f))
        if (entity.countdown != null && entity.countdown!! < 5) {
            posStack.rotate(Axis.X_NEGATIVE.rotationDegrees(entity.getPitch(tickDelta) + (60f * (6 - entity.countdown!!))))
        }

        itemRenderer.renderItem(
            DUMMY_STACK, ModelTransformationMode.FIXED, false,
            posStack, consumers, light, OverlayTexture.DEFAULT_UV,
            astralBladeModel
        )

        posStack.pop()
        super.render(entity, yaw, tickDelta, posStack, consumers, light)
    }


    @Suppress("DEPRECATION")
    override fun getTexture(entity: AstralProjectionEntity): Identifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE

    companion object {
        val DUMMY_STACK: ItemStack = Items.DIAMOND_SWORD.defaultStack
    }
}
