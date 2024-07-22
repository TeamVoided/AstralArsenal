package org.teamvoided.astralarsenal.handlers

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context
import net.minecraft.particle.ParticleTypes
import org.teamvoided.astralarsenal.networking.LaserBeamPayload

object LaserBeamPayloadHandler {
    fun handle(payload: LaserBeamPayload, ctx: Context) {
        val start = payload.start
        val end = payload.end
        val player = ctx.player()

        val particleCount = player.pos.distanceTo(end).toInt() * 2
        (0..particleCount).forEach { i ->
            val pos = start.lerp(end, i / particleCount.toDouble())
            player.world.addParticle(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
        }
    }
}