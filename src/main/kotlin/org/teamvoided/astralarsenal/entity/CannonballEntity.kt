package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ProjectileDeflector
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems

class CannonballEntity : ThrownItemEntity {
    constructor(entityType: EntityType<out CannonballEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.CANNONBALL_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.CANNONBALL_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return AstralItems.ASTRAL_GREATHAMMER
    }

        override fun method_59859(
            projectileDeflector: ProjectileDeflector,
            entity2: Entity?,
            entity: Entity?,
            bl: Boolean
        ): Boolean {
            if (!world.isClient) {
                deflecting.deflect(this, entity2, this.random)
                this.owner = entity
                this.method_59525(entity2, bl)
            }

            return true
        }
    companion object {
        val deflecting: ProjectileDeflector =
            ProjectileDeflector { projectileEntity: ProjectileEntity, entity: Entity?, random: RandomGenerator? ->
                if (entity != null) {
                    val vec3d = entity.rotationVector.normalize().multiply(2.0)
                    projectileEntity.velocity = vec3d
                    projectileEntity.velocityDirty = true
                }
            }
    }
}