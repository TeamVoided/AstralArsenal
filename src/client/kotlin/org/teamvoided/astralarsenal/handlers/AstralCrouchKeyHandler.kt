package org.teamvoided.astralarsenal.handlers

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.networking.SlamKosmogliphPayload

class AstralCrouchKeyHandler : ClientTickEvents.EndTick {
    var holdingJump = false

    fun jump() {
        ClientPlayNetworking.send(SlamKosmogliphPayload)
    }

    override fun onEndTick(client: MinecraftClient?) {
        if (client == null) return

        if(!client.options.sneakKey.isPressed)
            holdingJump = false
        else if(client.options.sneakKey.isPressed && !holdingJump) {
            jump()
            holdingJump = true
        }
    }
}