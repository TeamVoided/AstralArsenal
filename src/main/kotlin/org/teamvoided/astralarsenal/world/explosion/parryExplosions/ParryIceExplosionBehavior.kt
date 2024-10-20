package org.teamvoided.astralarsenal.world.explosion.parryExplosions

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class ParryIceExplosionBehavior(causingEntity: Entity) : ExplosionBehavior() {
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
        return 0.5f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        entity.frozenTicks += 1000
        if (entity is LivingEntity && entity != causingEntity) {
            entity.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.PARRY),
                    causingEntity,
                    causingEntity
                ), 10f
            )
        }
        return 0f
    }
}