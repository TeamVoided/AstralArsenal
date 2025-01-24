package org.teamvoided.astralarsenal.pseudomixin

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import org.teamvoided.astralarsenal.util.getKosmogliphs

fun airSpeedKosmogliphCall(entity: LivingEntity, speed: Float): Float {
    val stacks = EquipmentSlot.entries.map { entity.getEquippedStack(it) }

    var speedOut = speed
    stacks.forEach { stack ->
        stack.getKosmogliphs().forEach { speedOut = it.modifyAirStrafeSpeed(entity, speedOut) }
    }

    return speedOut
}