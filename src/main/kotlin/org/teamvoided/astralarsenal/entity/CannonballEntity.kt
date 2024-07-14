package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
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
        return AstralItems.CANNONBALL
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        entityHitResult.entity.damage(this.damageSources.thrown(this, owner), this.getDmg().toFloat())
        this.playSound(SoundEvents.ITEM_MACE_SMASH_AIR)
        val i: Int = this.getDmg() + 5
        this.setDmg(i)
        if(entityHitResult.entity.isAlive) {
            this.setVelocity(this.getVelocity().multiply(-0.1, 0.0, -0.1))
            this.addVelocity(0.0, 0.2, 0.0)
        }
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
                    projectileEntity.playSound(SoundEvents.ITEM_MACE_SMASH_AIR)
                }
            }
        private val DMG: TrackedData<Int>? =
            DataTracker.registerData(CannonballEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(DMG, 5)
    }

    fun setDmg(dmg: Int) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Int {
        return dataTracker.get(DMG) as Int
    }
}