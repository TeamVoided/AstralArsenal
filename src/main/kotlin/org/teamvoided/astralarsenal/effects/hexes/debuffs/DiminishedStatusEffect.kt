package org.teamvoided.astralarsenal.effects.hexes.debuffs

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.particle.ParticleEffect
import org.teamvoided.astralarsenal.init.AstralEffects
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

class DiminishedStatusEffect  : StatusEffect {
    constructor(type: StatusEffectType, color: Int) : super(type, color)
    constructor(type: StatusEffectType, color: Int, particle: ParticleEffect) : super(type, color, particle)

    val hexes = listOf(
        AstralEffects.BREACHED
    )

    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    val unhealable = listOf(
        AstralEffects.HARD_DAMAGE
    )

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        val effects = entity.statusEffects.filter { hexes.contains(it.effectType) }
        if (effects.isNotEmpty() && (entity.world.time % 6) + 1 == 0L){
            for (effect in effects){
                entity.removeStatusEffect(effect.effectType)
            }
        }
        if(entity.world.time % 10 == 0L && entity.health + 0.1 <= entity.maxHealth){
            val healthMissing = (entity.maxHealth - entity.health)
            var hard_levels = min(floor(healthMissing * 10).toInt(), amplifier + 1)
            val effects = entity.statusEffects.filter { unhealable.contains(it.effectType) }
            if (effects.isNotEmpty()) {
                effects.forEach {
                    val w = it.amplifier
                    hard_levels += w
                }
            }
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.HARD_DAMAGE,
                    200, min(hard_levels, 255),
                    false, true, true
                )
            )
        }
        else if(entity.world.time % 10 == 0L){

            val effects = entity.statusEffects.filter { unhealable.contains(it.effectType) }
            if (effects.isNotEmpty()) {
                effects.forEach {
                    val w = it.amplifier
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.HARD_DAMAGE,
                            200, w,
                            false, true, true
                        )
                    )
                }
            }
        }
        return super.applyUpdateEffect(entity, amplifier)
    }

}