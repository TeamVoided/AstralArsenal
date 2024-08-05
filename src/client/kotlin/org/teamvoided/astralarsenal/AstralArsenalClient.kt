package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.astralarsenal.block.entity.CosmicTableBlockEntityRenderer
import org.teamvoided.astralarsenal.handlers.KeyHandlers
import org.teamvoided.astralarsenal.handlers.LaserBeamPayloadHandler
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.init.AstralEntitiesClient
import org.teamvoided.astralarsenal.init.AstralHandledScreens
import org.teamvoided.astralarsenal.init.AstralHudRendering
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.networking.LaserBeamPayload
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        AstralHandledScreens
        AstralKeyBindings
        AstralEntitiesClient.clientInit()
        MinecraftClient.getInstance()

        ClientTickEvents.END_CLIENT_TICK.register(KeyHandlers.compileHandlers())

        ClientPlayNetworking.registerGlobalReceiver(LaserBeamPayload.ID, LaserBeamPayloadHandler::handle)

        ItemTooltipCallback.EVENT.register { stack, tooltipContext, tooltipType, lines ->
            getKosmogliphsOnStack(stack).forEach { kosmogliph: Kosmogliph ->
                kosmogliph.modifyItemTooltip(
                    stack,
                    tooltipContext,
                    lines,
                    tooltipType
                )
            }
        }

        AstralHudRendering.init()
        BlockEntityRendererFactories.register(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, ::CosmicTableBlockEntityRenderer)
    }
}
