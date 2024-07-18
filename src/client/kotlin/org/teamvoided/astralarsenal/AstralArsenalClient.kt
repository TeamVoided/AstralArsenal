package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.init.AstralEntitiesClient
import org.teamvoided.astralarsenal.init.AstralHandledScreens
import org.teamvoided.astralarsenal.networking.JumpKosmogliphPayload

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        AstralHandledScreens
        AstralEntitiesClient.clientInit()
        MinecraftClient.getInstance()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (client.options.jumpKey.isPressed) {
                ClientPlayNetworking.send(JumpKosmogliphPayload)
            }
        }
    }
}
