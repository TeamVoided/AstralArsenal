package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Holder
import net.minecraft.world.explosion.Explosion
import org.teamvoided.astralarsenal.init.AstralEffects

class RancidExplosionBehavior : BlockSafeExplosionBehavior() {
    override fun getKnockbackMultiplier(target: Entity): Float = 2.5f
    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity) {
            entity.addEffect(StatusEffects.SLOWNESS)
            entity.addEffect(StatusEffects.POISON)
            entity.addEffect(StatusEffects.WEAKNESS)
            entity.addEffect(AstralEffects.REDUCE)
            entity.addEffect(AstralEffects.BLEED)
        }
        return 0f
    }

    fun LivingEntity.addEffect(type: Holder<StatusEffect>, duration: Int = 600, amplifier: Int = 0) =
        addStatusEffect(StatusEffectInstance(type, duration, amplifier, false, true, true))
}