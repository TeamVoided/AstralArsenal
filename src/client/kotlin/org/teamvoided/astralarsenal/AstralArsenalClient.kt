package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.astralarsenal.block.entity.KosmicTableBlockEntityRenderer
import org.teamvoided.astralarsenal.entity.HexRingModel
import org.teamvoided.astralarsenal.entity.HexRingRenderer
import org.teamvoided.astralarsenal.handlers.KeyHandlers
import org.teamvoided.astralarsenal.init.*

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

        EntityModelLayerRegistry.registerModelLayer(
            HexRingModel.MODEL_LAYER,
            HexRingModel::createLayer
        )
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { type, renderer, registrationHelper, context ->
            registrationHelper!!.register(HexRingRenderer(renderer, context!!))
        }
    }
}
