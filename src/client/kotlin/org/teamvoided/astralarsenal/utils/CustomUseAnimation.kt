package org.teamvoided.astralarsenal.utils

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.Axis
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.creative_works.client.DebugWidgetRegistry.addFloat

object CustomUseAnimation {

    // (ender) ID for model that you want to render
    val PAGE_MODEL_ID = id("item/eat_yum_yum_page")

    // (ender) model that get cashed so ity doest have to be gotten form the manager every frame
    var pageModel: BakedModel? = null
        get() {
            if (field == null) {
                field = MinecraftClient.getInstance().bakedModelManager.getModel(PAGE_MODEL_ID)
            }
            return field
        }

    fun init() {
        ModelLoadingPlugin.register {
            // (ender) Loads the custom model so it can be accessed from the manager
            it.addModels(PAGE_MODEL_ID /*(ender) it's a vararg so you can just add more right here if needed*/)
        }
        // (ender) ResourcePackReloadEvent set `pageModel` to be null when Resource packs reload so things don't break
        // if you add more model remember to also reset them
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(ResourcePackReloadEvent)
    }

    // --- DEBUGGING CODE ---

    // (ender)
    // Adds an angle widget to CW's widget menu
    // (Can be opened with `v` by default)
    val rotation = addFloat("angle", 90f)

    // --- END OF DEBUGGING CODE ---

    // (ender) Don't touch this or the mixin
    @Suppress("unused")
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
        renderCustomPage(
            player, tickDelta, stack, matrices, vertexConsumers, light, modelTransformationMode, isLeftHand
        )
    }


    private fun renderCustomPage(
        player: AbstractClientPlayerEntity,
        tickDelta: Float,
        stack: ItemStack,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        modelTransformationMode: ModelTransformationMode,
        isLeftHand: Boolean,
    ) {
        if (stack.isEmpty) return
        if (!stack.isOf(AstralItems.EAT_YUM_YUM)) return
        // (ender) checks if player is using item
        if (!player.isUsingItem) return

        matrices.push()
        val usageTicks = player.itemUseTimeLeft
        // (ender) display usage ticks, if this is 0 and doesn't change you don't have use ticks implemented
        player.sendMessage(Text.literal("UsageTicks: $usageTicks"), true)

        // (ender) does the funny ration
        matrices.rotateAround(
            // (ender) the rotation
            Axis.Y_NEGATIVE.rotationDegrees(rotation.get()),
            // (ender) rotates around this point
            0f, 0f, 0f
        )

        // (ender) Renders a custom model as item model
        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack, modelTransformationMode, isLeftHand,
            matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV,
            pageModel
        )
        matrices.pop()
    }

}