package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior

class KnockbackExplosionBehavior : ExplosionBehavior() {

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
        return false
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        target.addVelocity(0.0,2.0,0.0)
        return 0f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        return 0f
    }
}