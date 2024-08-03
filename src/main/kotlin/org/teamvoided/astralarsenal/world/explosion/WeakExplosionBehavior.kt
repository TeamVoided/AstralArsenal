package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior

class WeakExplosionBehavior : ExplosionBehavior() {

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
        target.addVelocity(0.0,1.0,0.0)
        return 1f
    }

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (entity is LivingEntity){
            return 10f}
        else{return 0f}
    }
}