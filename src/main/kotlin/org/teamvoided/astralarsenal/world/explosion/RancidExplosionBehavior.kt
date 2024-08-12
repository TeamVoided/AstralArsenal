package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralEffects

class RancidExplosionBehavior : ExplosionBehavior() {

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
        return 2.5f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        if (entity is LivingEntity) {
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    600, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.POISON,
                    600, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    600, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.REDUCE,
                    600, 0,
                    false, true, true
                )
            )
            entity.addStatusEffect(
                StatusEffectInstance(
                    AstralEffects.BLEED,
                    600, 0,
                    false, true, true
                )
            )
        }
        return 0f
    }
}