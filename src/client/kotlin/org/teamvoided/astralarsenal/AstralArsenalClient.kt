package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.Axis
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.block.entity.KosmicTableBlockEntityRenderer
import org.teamvoided.astralarsenal.handlers.KeyHandlers
import org.teamvoided.astralarsenal.init.*
import org.teamvoided.creative_works.client.DebugWidgetRegistry

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        AstralHandledScreens
        AstralKeyBindings.init()
        AstralEntitiesClient.clientInit()
        AstralParticlesClient.init()
        MinecraftClient.getInstance()

        ClientTickEvents.END_CLIENT_TICK.register(KeyHandlers.compileHandlers())

        AstralHudRendering.init()
        BlockEntityRendererFactories.register(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, ::KosmicTableBlockEntityRenderer)

        ModelLoadingPlugin.register {
            it.addModels(id("item/eat_yum_yum_page"))
        }
    }

    val rotation = DebugWidgetRegistry.addFloat("angle", 0f);
    @JvmStatic
    fun invokeCustomPostRenderer(
        player: AbstractClientPlayerEntity,
        tickDelta: Float,
        pitch: Float,
        hand: Hand,
        swingProgress: Float,
        stack: ItemStack,
        equipProgress: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        modelTransformationMode: ModelTransformationMode,
        isLeftHand: Boolean,
        heldItemRenderer: HeldItemRenderer,
    ) {
        if (stack.isEmpty) return
        if (!stack.isOf(AstralItems.EAT_YUM_YUM)) return
//        if (!player.isUsingItem) return

        matrices.push()
        val usageTicks = player.itemUseTimeLeft
        player.sendMessage(Text.literal("UsageTicks: $usageTicks"), true)

        matrices.rotateAround(Axis.Y_NEGATIVE.rotationDegrees(/*usageTicks / -120f*/ rotation.get()), 0f, 0f, 0f)
        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack,
            modelTransformationMode,
            false,
            matrices,
            vertexConsumers,
            light,
            OverlayTexture.DEFAULT_UV,
            getPageModel()
        )
        matrices.pop()
    }


    private var pageModel: BakedModel? = null
    fun getPageModel(): BakedModel {
        if (pageModel == null) {
            pageModel = MinecraftClient.getInstance().bakedModelManager.getModel(id("item/eat_yum_yum_page"))
        }
        return pageModel!!
    }
}
