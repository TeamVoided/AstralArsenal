package org.teamvoided.astralarsenal.handlers

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.networking.JumpKosmogliphPayload

class AstralJumpKeyHandler : ClientTickEvents.EndTick {
    var holdingJump = false

    fun jump() {
        ClientPlayNetworking.send(JumpKosmogliphPayload)
    }

    override fun onEndTick(client: MinecraftClient?) {
        if (client == null) return

        if(client.player?.isOnGround == true)
            holdingJump = true
        else if(!client.options.jumpKey.isPressed)
            holdingJump = false
        else if(client.options.jumpKey.isPressed && !holdingJump) {
            jump()
            holdingJump = true
        }
    }
}
