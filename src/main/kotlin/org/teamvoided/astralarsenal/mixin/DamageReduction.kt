package org.teamvoided.astralarsenal.mixin

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import org.teamvoided.astralarsenal.init.AstralItemComponents

fun kosmogliphDamageReductionCall(entity: LivingEntity, damage: Float, source: DamageSource): Float {
    val stacks = EquipmentSlot.entries.associateWith { entity.getEquippedStack(it) }

    var resultingDamage = damage
    stacks.forEach { (slot, stack) ->
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
        kosmogliphs.forEach { resultingDamage = it.modifyDamage(stack, resultingDamage, source, slot) }
    }

    return resultingDamage
}
