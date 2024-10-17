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

class ParryFireExplosionBehavior : ExplosionBehavior() {

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
        entity.setOnFireFor(200)
            if (entity is LivingEntity && entity != explosion.causingEntity) {
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.BOOM),
                        explosion.causingEntity,
                        explosion.causingEntity
                    ), 10f
                )
            }
        return 0f
    }
}