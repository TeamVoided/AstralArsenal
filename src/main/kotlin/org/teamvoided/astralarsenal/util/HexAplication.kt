package org.teamvoided.astralarsenal.util

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import org.teamvoided.astralarsenal.init.AstralEffects.BLAZED
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHING
import org.teamvoided.astralarsenal.init.AstralEffects.CLEANSED
import org.teamvoided.astralarsenal.init.AstralEffects.DIMINISHED
import org.teamvoided.astralarsenal.init.AstralEffects.IMPEDED
import org.teamvoided.astralarsenal.init.AstralEffects.WEAKENED

val hexes = listOf(
    BREACHED,
    DIMINISHED,
    WEAKENED,
    BLAZED,
    CLEANSED,
    IMPEDED
)

val hexAppliers = listOf(
    BREACHING
)

val breaching = listOf(
    BREACHING
)

fun applyHexes(source: DamageSource, entity: LivingEntity) {
    if (source.attacker is LivingEntity) {
        val attacker = source.attacker as LivingEntity
        val effect_breaching = attacker.statusEffects.filter { breaching.contains(it.effectType) }
        for (effect in effect_breaching) {
            for (effects in hexes) {
                entity.removeStatusEffect(effects)
            }
            entity.addStatusEffect(StatusEffectInstance(BREACHED, 100, effect.amplifier))
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

fun tickDownPositiveEffects(entity: LivingEntity){
    val effects = entity.statusEffects.filter { cleaned.contains(it.effectType) }
    for (effect in effects){
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