package org.teamvoided.astralarsenal.handlers

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.EquipmentSlot
import org.teamvoided.astralarsenal.handlers.KeyHandlers.ClientCtxInvokable
import org.teamvoided.astralarsenal.init.AstralKeyBindings
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.networking.DashKosmogliphPayload
import org.teamvoided.astralarsenal.networking.DodgeKosmogliphPayload
import org.teamvoided.astralarsenal.networking.JumpKosmogliphPayload
import org.teamvoided.astralarsenal.networking.SlamKosmogliphPayload
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
            AstralKosmogliphs.JUMP.handleJump(client.player!!.getEquippedStack(EquipmentSlot.FEET), client.player!!)
            holdingJump.value = true
        }
    }

    private fun crouchHandler(holdingCrouch: AtomicBoolean) = ClientCtxInvokable { client: MinecraftClient ->
        val player = client.player ?: return@ClientCtxInvokable
        if (player.isOnGround || player.isCreative || player.isSpectator) {
            holdingCrouch.value = true
        } else if (!client.options.sneakKey.isPressed) {
            holdingCrouch.value = false
        } else if (!holdingCrouch.value) {
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

            val forward = client.options.forwardKey.isPressed
            val backward = client.options.backKey.isPressed
            val left = client.options.leftKey.isPressed
            val right = client.options.rightKey.isPressed
            AstralKosmogliphs.DODGE.handleJump(
                client.player!!.getEquippedStack(EquipmentSlot.LEGS),
                client.player!!,
                forward,
                backward,
                left,
                right
            )
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
