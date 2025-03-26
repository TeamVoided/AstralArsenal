package org.teamvoided.astralarsenal.effects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectType
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage

class BleedStatusEffect(color: Int) : AstralStatusEffect(StatusEffectType.HARMFUL, color) {
    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity?, amplifier: Int): Boolean {
        if (entity != null && entity.world != null && (entity.world.time % 20 == 0L)) {
            entity.customDamage(AstralDamageTypes.BLEED, (0.4 * (amplifier + 1)).toFloat())
        }
        return true
    }
}