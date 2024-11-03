package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class CometExplosionBehavior(causingEntity: Entity?, hitEntity: Entity?, inputDamage: Float) : ExplosionBehavior() {
    val cause = causingEntity
    val hit = hitEntity
    val damage = inputDamage
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
        var dmg = damage
        if(hit != null && entity == hit){
            dmg = dmg * 1.5f
        }
        return dmg
    }
}