package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.LivingEntity
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

interface AirSpeedKosmogliph : Kosmogliph {
    companion object {
        val AIR_STRAFE_MODIFIER = 1.0f
    }
    override fun modifyAirStrafeSpeed(entity: LivingEntity, speed: Float): Float =
        entity.movementSpeed * AIR_STRAFE_MODIFIER
}