package org.teamvoided.astralarsenal.effects.hexes.debuffs

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.particle.ParticleEffect
import org.teamvoided.astralarsenal.init.AstralEffects

class BreachedStatusEffect : StatusEffect {
    constructor(type: StatusEffectType, color: Int) : super(type, color)
    constructor(type: StatusEffectType, color: Int, particle: ParticleEffect) : super(type, color, particle)

    val hexes = listOf(
        AstralEffects.BLEED
    )

    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        val effects = entity.statusEffects.filter { hexes.contains(it.effectType) }
        if (effects.isNotEmpty() && entity.world.time % 6 == 0L){
            for (effect in effects){
                entity.removeStatusEffect(effect.effectType)
            }
        }
        entity.removeStatusEffect(AstralEffects.BLEED)
        return super.applyUpdateEffect(entity, amplifier)
    }

}