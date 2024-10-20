package org.teamvoided.astralarsenal.world.explosion.parryExplosions

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects

class ParryBrewExplosionBehavior(causingEntity: Entity) : ExplosionBehavior() {
    val causingEntity = causingEntity
    override fun canDestroyBlock(
        explosion: Explosion,
        world: BlockView,
        pos: BlockPos,
        state: BlockState,
        power: Float
    ): Boolean {
        return false
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return 1.0f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        if (entity is LivingEntity) {
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    100, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.POISON,
                    100, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    100, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.REDUCE,
                    100, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.BLEED,
                    100, 0,
                    false, true, true
                )
            )
        }
        if (entity is LivingEntity && entity != causingEntity) {
            entity.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(entity.world.registryManager, DamageTypes.MAGIC),
                    causingEntity,
                    causingEntity
                ), 5f
            )
        }
        return 0f
    }
}