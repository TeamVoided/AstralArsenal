package org.teamvoided.astralarsenal.pseudomixin

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.util.getKosmogliphs
import kotlin.math.max

val impeded = listOf(AstralEffects.IMPEDED)

fun airSpeedKosmogliphCall(entity: LivingEntity, speed: Float): Float {
    val stacks = EquipmentSlot.entries.map { entity.getEquippedStack(it) }

    var speedOut = speed
    stacks.forEach { stack ->
        stack.getKosmogliphs().forEach { speedOut = it.modifyAirStrafeSpeed(entity, speedOut) }
    }
    val effects = entity.statusEffects.filter { impeded.contains(it.effectType) }
    for (effect in effects){
        val reduction = max(1 - (0.25f * (effect.amplifier + 1)), 0f)
        speedOut *= reduction
    }

    return speedOut
}