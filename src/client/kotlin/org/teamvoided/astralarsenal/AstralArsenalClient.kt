package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.handlers.AstralJumpKeyHandler
import org.teamvoided.astralarsenal.handlers.AstralSprintKeyHandler
import org.teamvoided.astralarsenal.init.AstralEntitiesClient
import org.teamvoided.astralarsenal.init.AstralHandledScreens

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        AstralHandledScreens
        AstralEntitiesClient.clientInit()
        MinecraftClient.getInstance()

        ClientTickEvents.END_CLIENT_TICK.register(AstralJumpKeyHandler())
        ClientTickEvents.END_CLIENT_TICK.register(AstralSprintKeyHandler())
    }
}
