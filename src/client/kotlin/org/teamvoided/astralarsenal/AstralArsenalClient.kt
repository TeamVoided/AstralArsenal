package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.astralarsenal.block.entity.KosmicTableBlockEntityRenderer
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
    }
}
