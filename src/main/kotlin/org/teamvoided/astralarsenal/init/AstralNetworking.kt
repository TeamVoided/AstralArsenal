package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EquipmentSlot
import org.teamvoided.astralarsenal.networking.*
import org.teamvoided.astralarsenal.util.hasKosmogliph

object AstralNetworking {
    fun init() {
        PayloadTypeRegistry.playS2C().register(LaserBeamPayload.ID, LaserBeamPayload.CODEC)

        PayloadTypeRegistry.playC2S().register(JumpKosmogliphPayload.ID, JumpKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(JumpKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.FEET)
            if (!stack.hasKosmogliph(AstralKosmogliphs.JUMP)) return@registerGlobalReceiver
            AstralKosmogliphs.JUMP.handleJump(stack, player)
        }
        PayloadTypeRegistry.playC2S().register(DashKosmogliphPayload.ID, DashKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(DashKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.LEGS)
            if (!stack.hasKosmogliph(AstralKosmogliphs.DASH)) return@registerGlobalReceiver
            AstralKosmogliphs.DASH.handleJump(stack, player)
        }
        PayloadTypeRegistry.playC2S().register(DodgeKosmogliphPayload.ID, DodgeKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(DodgeKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.LEGS)
            if (!stack.hasKosmogliph(AstralKosmogliphs.DODGE)) return@registerGlobalReceiver
            AstralKosmogliphs.DODGE.handleJump(stack, player, false, false, false, false)
        }
        PayloadTypeRegistry.playC2S().register(SlamKosmogliphPayload.ID, SlamKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(SlamKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.HEAD)
            if (!stack.hasKosmogliph(AstralKosmogliphs.SLAM)) return@registerGlobalReceiver
            AstralKosmogliphs.SLAM.handleSlam(stack, player)
        }
    }
}