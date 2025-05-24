package org.teamvoided.astralarsenal.util

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZED
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZING
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHING
import org.teamvoided.astralarsenal.init.AstralEffects.CLEANSED
import org.teamvoided.astralarsenal.init.AstralEffects.CLEANSING
import org.teamvoided.astralarsenal.init.AstralEffects.DIMINISHED
import org.teamvoided.astralarsenal.init.AstralEffects.DIMINISHING
import org.teamvoided.astralarsenal.init.AstralEffects.IMPEDED
import org.teamvoided.astralarsenal.init.AstralEffects.IMPEDING
import org.teamvoided.astralarsenal.init.AstralEffects.MAGNETISED
import org.teamvoided.astralarsenal.init.AstralEffects.MAGNETISING
import org.teamvoided.astralarsenal.init.AstralEffects.UNHEALABLE_DAMAGE
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENED
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENING

val hexes = listOf(
    BREACHED,
    DIMINISHED,
    WEAKENED,
    BLAZED,
    CLEANSED,
    IMPEDED,
    MAGNETISED
)

val hexAppliers = listOf(
    BREACHING,
    DIMINISHING,
    WEAKENING,
    BLAZING,
    CLEANSING,
    IMPEDING,
    MAGNETISING
)

fun applyHexes(source: DamageSource, entity: LivingEntity) {
    if (source.attacker is LivingEntity) {
        val attacker = source.attacker as LivingEntity
        val attackerEffects = attacker.statusEffects.filter { hexAppliers.contains(it.effectType) }
        for (effect in attackerEffects) {
            val hexNumber = hexAppliers.indexOf(effect.effectType)
            entity.addStatusEffect(
                StatusEffectInstance(
                    hexes.get(hexNumber),
                    100,
                    effect.amplifier,
                    false,
                    false,
                    true
                )
            )
        }
    }
}

fun removeHexes(entity: LivingEntity) {
    var effects = entity.statusEffects.filter { hexes.contains(it.effectType) }
    if (effects.size > 1) {
        repeat(effects.size - 1) {
            val random = entity.world.random.rangeInclusive(0, effects.size - 1)
            entity.removeStatusEffect(effects[random].effectType)
        }
    }
    effects = entity.statusEffects.filter { hexAppliers.contains(it.effectType) }
    if (effects.size > 2) {
        repeat(effects.size - 1) {
            val random = entity.world.random.rangeInclusive(0, effects.size - 1)
            entity.removeStatusEffect(effects[random].effectType)
        }
    }
}

val cleaned = listOf(
    CLEANSED
)

fun tickDownPositiveEffects(entity: LivingEntity) {
    val effects = entity.statusEffects.filter { cleaned.contains(it.effectType) }
    for (effect in effects) {
        val decline = effect.amplifier + 1
        val positiveEffects = entity.statusEffects.filter { it.effectType.value().isBeneficial }
        positiveEffects.forEach { posEffect ->
            entity.statusEffects.remove(posEffect)
            entity.addStatusEffect(
                StatusEffectInstance(
                    posEffect.effectType,
                    posEffect.duration - decline, posEffect.amplifier,
                    posEffect.isAmbient, posEffect.shouldShowParticles(), posEffect.shouldShowIcon()
                )
            )
        }
    }
}

val diminished = listOf(
    DIMINISHED
)

val hardDamage = listOf(
    UNHEALABLE_DAMAGE
)

fun tickDownHealth(entity: LivingEntity) {
    val effects = entity.statusEffects.filter { diminished.contains(it.effectType) }
    for (effect in effects) {
        if (entity.world.time % (100 / (effect.amplifier + 1)) == 0L && entity.health <= entity.maxHealth - 1) {
            val diminishedEffects = entity.statusEffects.filter { hardDamage.contains(it.effectType) }
            if (diminishedEffects.isNotEmpty()) {
                for (hardDmg in diminishedEffects) {
                    entity.statusEffects.remove(hardDmg)
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            UNHEALABLE_DAMAGE,
                            402, hardDmg.amplifier + 1,
                            false, true, true
                        )
                    )
                }
            } else {
                entity.addStatusEffect(
                    StatusEffectInstance(
                        UNHEALABLE_DAMAGE,
                        402, 0,
                        false, true, true
                    )
                )
            }
        } else{
            val diminishedEffects = entity.statusEffects.filter { hardDamage.contains(it.effectType) }
            if (diminishedEffects.isNotEmpty()) {
                for (hardDmg in diminishedEffects) {
                    entity.statusEffects.remove(hardDmg)
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            UNHEALABLE_DAMAGE,
                            402, hardDmg.amplifier,
                            false, true, true
                        )
                    )
                }
            }
        }
    }
}