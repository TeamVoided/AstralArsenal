package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ProjectileDeflector
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.world.explosion.KnockbackExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.StrongExplosionBehavior
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior

class MortarEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out MortarEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.MORTAR_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.MORTAR_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return AstralItems.MORTER
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity.isOnGround) {
            world.createExplosion(
                this,
                damageSources.explosion(this, this.owner),
                this.owner?.let { WeakExplosionBehavior(it) },
                this.x,
                this.y,
                this.z,
                2.0f,
                false,
                World.ExplosionSourceType.TNT
            )
            for (i in 0..100) {
                world.addParticle(
                    ParticleTypes.FLAME,
                    true,
                    this.x + random.rangeInclusive(-1, 1).times(0.1),
                    this.y + random.rangeInclusive(-1, 1).times(0.1),
                    this.z + random.rangeInclusive(-1, 1).times(0.1),
                    random.nextDouble().times(2).minus(1).times(0.01),
                    random.nextDouble().times(0.1),
                    random.nextDouble().times(2).minus(1).times(0.01)
                )
            }
            this.discard()
        } else {
            world.createExplosion(
                this,
                damageSources.explosion(this, this.owner),
                this.owner?.let { StrongExplosionBehavior(it) },
                this.x,
                this.y,
                this.z,
                3.0f,
                false,
                World.ExplosionSourceType.TNT
            )
            this.discard()
        }
        super.onEntityHit(entityHitResult)
    }

    override fun deflect(
        projectileDeflector: ProjectileDeflector,
        entity2: Entity?,
        entity: Entity?,
        bl: Boolean
    ): Boolean {
        if (!world.isClient) {
            deflecting.deflect(this, entity2, this.random)
            this.owner = entity
            this.onDeflected(entity2, bl)
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
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
    }

    override fun tick() {
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        world.createExplosion(
            null,
            damageSources.explosion(null, this.owner),
            KnockbackExplosionBehavior(this.owner),
            this.x,
            this.y,
            this.z,
            2.0f,
            false,
            World.ExplosionSourceType.TNT
        )
        this.discard()
        super.onBlockHit(blockHitResult)
    }

}