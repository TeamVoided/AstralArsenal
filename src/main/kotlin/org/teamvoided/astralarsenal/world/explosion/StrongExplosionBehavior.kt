package org.teamvoided.astralarsenal.world.explosion

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes

class StrongExplosionBehavior(causingEntity: Entity) : ExplosionBehavior() {
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
        return 3f
    }

    override fun calculateDamage(explosion: Explosion?, entity: Entity?): Float {
        if (explosion != null) {
            if (entity is PlayerEntity) {
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.BOOM),
                        causingEntity,
                        causingEntity
                    ), 15f
                )
            }
            else if(entity is LivingEntity){
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(entity.world.registryManager, AstralDamageTypes.BOOM),
                        causingEntity,
                        causingEntity
                    ), 30f
                )
            }
        }
        return 0f
    }
}