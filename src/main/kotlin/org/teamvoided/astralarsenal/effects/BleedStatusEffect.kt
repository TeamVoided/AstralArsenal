package org.teamvoided.astralarsenal.effects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectType
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import kotlin.math.max

class BleedStatusEffect(color: Int) : AstralStatusEffect(StatusEffectType.HARMFUL, color) {
    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return tick % max(1, 30 - (amplifier * 20)) == 0
    }

    override fun applyUpdateEffect(entity: LivingEntity?, amplifier: Int): Boolean {
        entity?.customDamage(AstralDamageTypes.BLEED, 1.0f)
        return true
    }
}