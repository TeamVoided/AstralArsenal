package org.teamvoided.astralarsenal.world.explosion.maceExplosions

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class MaceWeakExplosionBehavior(causingEntity: Entity) : ExplosionBehavior() {
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

    override fun getKnockbackMultiplier(target: Entity): Float {
        return if (target == cause) 0.0f else 2.5f
    }

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (explosion != null) {
            if (entity is LivingEntity) {
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.BOOM),
                        explosion.causingEntity,
                        explosion.causingEntity
                    ), 5f
                )
            }
        }
        return 0f
    }
}