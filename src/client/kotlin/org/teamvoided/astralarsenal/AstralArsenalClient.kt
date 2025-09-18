package org.teamvoided.astralarsenal

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.RegisterType
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.astralarsenal.block.entity.KosmicTableBlockEntityRenderer
import org.teamvoided.astralarsenal.config.AAClientConfig
import org.teamvoided.astralarsenal.entity.HexRingModel
import org.teamvoided.astralarsenal.entity.HexRingRenderer
import org.teamvoided.astralarsenal.handlers.KeyHandlers
import org.teamvoided.astralarsenal.init.*
import org.teamvoided.astralarsenal.networking.UpdateHexRingPayload
import org.teamvoided.astralarsenal.util.EntityHexAccessor
import org.teamvoided.astralarsenal.utils.CustomUseAnimation

@Suppress("unused")
object AstralArsenalClient {

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::AAClientConfig, RegisterType.CLIENT)

    fun init() {
        AstralHandledScreens.init()
        AstralKeyBindings.init()
        AstralEntitiesClient.clientInit()
        AstralParticlesClient.init()
        MinecraftClient.getInstance()

        ClientTickEvents.END_CLIENT_TICK.register(KeyHandlers.compileHandlers())
        CustomUseAnimation.init()

        AstralHudRendering.init()
        BlockEntityRendererFactories.register(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, ::KosmicTableBlockEntityRenderer)

        EntityModelLayerRegistry.registerModelLayer(
            HexRingModel.MODEL_LAYER,
            HexRingModel::createLayer
        )
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { type, renderer, registrationHelper, context ->
            registrationHelper!!.register(HexRingRenderer(renderer, context!!))
        }
        ClientPlayNetworking.registerGlobalReceiver(UpdateHexRingPayload.ID, { packet, context ->
            (context.player().world.getEntityById(packet.entityId) as EntityHexAccessor).`setAstralArsenal$hexColor`(packet.color)
        })
    }
}
