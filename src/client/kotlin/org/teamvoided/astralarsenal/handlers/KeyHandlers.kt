package org.teamvoided.astralarsenal.handlers

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import org.teamvoided.astralarsenal.AstralKeyBindings
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.networking.*
import kotlin.reflect.full.declaredMemberProperties

object KeyHandlers {
    val jumpHandler = jumpHandler(atomic(false))
    val crouchHandler = crouchHandler(atomic(false))
    val sprintKeyHandler = sprintKeyHandler(atomic(false))

    fun compileHandlers(): ClientCtxInvokable {
        val invokeable = KeyHandlers::class.declaredMemberProperties.mapNotNull { it.get(this) as? ClientCtxInvokable }
        return ClientCtxInvokable { client -> invokeable.forEach { it(client) } }
    }

    private fun jumpHandler(holdingJump: AtomicBoolean) = ClientCtxInvokable { client: MinecraftClient ->
        val player = client.player ?: return@ClientCtxInvokable
        val jumpKey = client.options.jumpKey

        if (player.isOnGround || player.isCreative || player.isSpectator) {
            holdingJump.value = true
        } else if (!jumpKey.isPressed) {
            holdingJump.value = false
        } else if (!holdingJump.value) {
                ClientPlayNetworking.send(JumpKosmogliphPayload)
                holdingJump.value = true
        }
    }

    private fun crouchHandler(holdingCrouch: AtomicBoolean) = ClientCtxInvokable { client: MinecraftClient ->
        val player = client.player ?: return@ClientCtxInvokable
        if (player.isOnGround || player.isCreative || player.isSpectator) {
            holdingCrouch.value = true
        }
        else if (!client.options.sneakKey.isPressed) {
            holdingCrouch.value = false
        }
        else if (!holdingCrouch.value) {
            ClientPlayNetworking.send(SlamKosmogliphPayload)
            holdingCrouch.value = true
        }
    }

    private fun sprintKeyHandler(holdingSprint: AtomicBoolean) = ClientCtxInvokable { client: MinecraftClient ->
        val key =
            if (AstralKeyBindings.dashAbility.keyEquals(client.options.sprintKey)) client.options.sprintKey
            else AstralKeyBindings.dashAbility

        if (!key.isPressed) {
            holdingSprint.value = false
        } else if (key.isPressed && !holdingSprint.value) {
            ClientPlayNetworking.send(DashKosmogliphPayload)
            ClientPlayNetworking.send(DodgeKosmogliphPayload)
            holdingSprint.value = true
        }
//        else if (key.isPressed) {
//            ClientPlayNetworking.send(SlideKosmogliphPayload)
//        }
    }

    fun interface ClientCtxInvokable : ClientTickEvents.EndTick {
        operator fun invoke(client: MinecraftClient)
        override fun onEndTick(client: MinecraftClient) = invoke(client)
    }
}
