package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EquipmentSlot
import org.teamvoided.astralarsenal.networking.*

object AstralNetworking {
    fun init() {
        PayloadTypeRegistry.playS2C().register(LaserBeamPayload.ID, LaserBeamPayload.CODEC)

        PayloadTypeRegistry.playC2S().register(JumpKosmogliphPayload.ID, JumpKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(JumpKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.FEET)
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
            if (!kosmogliphs.contains(AstralKosmogliphs.JUMP)) return@registerGlobalReceiver
            AstralKosmogliphs.JUMP.handleJump(stack, player)
        }
        PayloadTypeRegistry.playC2S().register(DashKosmogliphPayload.ID, DashKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(DashKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.LEGS)
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
            if (!kosmogliphs.contains(AstralKosmogliphs.DASH)) return@registerGlobalReceiver
            AstralKosmogliphs.DASH.handleJump(stack, player)
        }
        PayloadTypeRegistry.playC2S().register(DodgeKosmogliphPayload.ID, DodgeKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(DodgeKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.LEGS)
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
            if (!kosmogliphs.contains(AstralKosmogliphs.DODGE)) return@registerGlobalReceiver
            AstralKosmogliphs.DODGE.handleJump(stack, player)
        }
        PayloadTypeRegistry.playC2S().register(SlamKosmogliphPayload.ID, SlamKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(SlamKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.HEAD)
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
            if (!kosmogliphs.contains(AstralKosmogliphs.SLAM)) return@registerGlobalReceiver
            AstralKosmogliphs.SLAM.handleSlam(stack, player)
        }
    }
}