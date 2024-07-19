package org.teamvoided.astralarsenal.handlers

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.networking.DashKosmogliphPayload
import org.teamvoided.astralarsenal.networking.JumpKosmogliphPayload

class AstralSprintKeyHandler : ClientTickEvents.EndTick {
    var holdingJump = false

    fun jump() {
        ClientPlayNetworking.send(DashKosmogliphPayload)
    }

    override fun onEndTick(client: MinecraftClient?) {
        if (client == null) return


        if(!client.options.sprintKey.isPressed)
            holdingJump = false
        else if(client.options.sprintKey.isPressed && !holdingJump) {
            jump()
            holdingJump = true
        }
    }
}