package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class KnockbackExplosionBehavior(causingEntity: Entity?) : ExplosionBehavior() {
    val cause = causingEntity
    override fun canDestroyBlock(
        explosion: Explosion,
        world: BlockView,
        pos: BlockPos,
        state: BlockState,
        power: Float
    ): Boolean {
        return false
    }

    override fun shouldDamage(explosion: Explosion, entity: Entity): Boolean {
        return cause == entity
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return 2.5f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        if (cause == entity){
        entity.damage(
            DamageSource(
                AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.BOOM),
                cause,
                cause
            ), 5f
        )}
        return 0f
    }
}