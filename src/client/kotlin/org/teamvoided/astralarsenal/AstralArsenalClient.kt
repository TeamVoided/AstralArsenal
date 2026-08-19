package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.gui.screen.ingame.HandledScreens
import org.teamvoided.astralarsenal.entity.HexRingModel
import org.teamvoided.astralarsenal.entity.HexRingRenderer
import org.teamvoided.astralarsenal.handlers.KeyHandlers
import org.teamvoided.astralarsenal.init.*
import org.teamvoided.astralarsenal.networking.UpdateHexRingPayload
import org.teamvoided.astralarsenal.screens.CosmicTableScreen
import org.teamvoided.astralarsenal.util.EntityHexAccessor
import org.teamvoided.astralarsenal.utils.CustomUseAnimation

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        HandledScreens.register(AstralMenus.COSMIC_TABLE, ::CosmicTableScreen)
        AstralKeyBindings.init()
        AstralRenderers.init()
        AstralParticlesClient.init()
        ClientTickEvents.END_CLIENT_TICK.register(KeyHandlers.compileHandlers())
        CustomUseAnimation.init()
        AstralHudRendering.init()

        EntityModelLayerRegistry.registerModelLayer(HexRingModel.MODEL_LAYER, HexRingModel::createLayer)
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { type, renderer, helper, ctx ->
            helper.register(HexRingRenderer(renderer, ctx))
        }

        ClientPlayNetworking.registerGlobalReceiver(UpdateHexRingPayload.ID) { packet, context ->
            (context.player().world.getEntityById(packet.entityId) as EntityHexAccessor)
                .`setAstralArsenal$hexColor`(packet.color)
        }
    }
}
