package org.teamvoided.astralarsenal.effects

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.particle.ParticleEffect

open class AstralStatusEffect : StatusEffect {

    val showTomeRings: Boolean

    constructor(type: StatusEffectType, color: Int, showTomeRings: Boolean = false) : super(type, color) {
        this.showTomeRings = showTomeRings
    }

    constructor(type: StatusEffectType, color: Int, particle: ParticleEffect, showTomeRings: Boolean = false) : super(
        type,
        color,
        particle
    ) {
        this.showTomeRings = showTomeRings
    }
}