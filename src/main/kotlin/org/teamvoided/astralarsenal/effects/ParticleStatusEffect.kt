package org.teamvoided.astralarsenal.effects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage

class ParticleStatusEffect(type: StatusEffectType, color: Int, particle: ParticleEffect) :
    AstralStatusEffect(type, color) {

    var particleEffect: ParticleEffect = particle

    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        val height = entity.height
        val width = entity.width
        if (entity.world is ServerWorld) {
            val world = entity.world as ServerWorld
            world.spawnParticles(
                particleEffect,
                entity.x,
                entity.y + (height/2),
                entity.z,
                1,
                width/2.5,
                height/2.5,
                width/2.5,
                0.0
            )
        }
        return true
    }
}