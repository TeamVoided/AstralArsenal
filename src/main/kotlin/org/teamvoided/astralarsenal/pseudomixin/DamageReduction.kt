package org.teamvoided.astralarsenal.pseudomixin

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.teamvoided.astralarsenal.init.AstralItemComponents

fun kosmogliphDamageReductionCall(entity: LivingEntity, damage: Float, source: DamageSource): Float {
    val stacks = EquipmentSlot.entries.associateWith { entity.getEquippedStack(it) }

    var resultingDamage = damage
    stacks.forEach { (slot, stack) ->
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
        kosmogliphs.forEach { resultingDamage = it.modifyDamage(stack, entity, resultingDamage, source, slot) }
    }

    return resultingDamage
}

fun kosmogliphInvulnerabilityCheck(entity: LivingEntity, source: DamageSource, ci: CallbackInfoReturnable<Boolean>) {
    val stacks = EquipmentSlot.entries.associateWith { entity.getEquippedStack(it) }
    stacks.forEach { (slot, stack) ->
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
        if (kosmogliphs.any { it.shouldNegateDamage(stack, entity, source, slot) }) ci.returnValue = true
    }
}
