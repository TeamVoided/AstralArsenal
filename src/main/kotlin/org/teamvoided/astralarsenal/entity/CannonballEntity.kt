package org.teamvoided.astralarsenal.entity

import com.sun.jdi.Type
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ProjectileDeflector
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TypeFilter
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.world.explosion.KnockbackExplosionBehavior

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
        entityHitResult.entity.customDamage(AstralDamageTypes.CANNONBALL,this.getDmg().toFloat())
        if(this.getDmg() in 20..39){
            this.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND)
        }
        else if(this.getDmg() >= 40){
            this.playSound((SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY))
            entityHitResult.entity.setOnFireFor(100)
        }
        else {
            this.playSound(SoundEvents.ITEM_MACE_SMASH_AIR)
        }
        var i: Int = this.getDmg() + 5
        if (i > 40){i = 40}
        this.setDmg(i)
        if (entityHitResult.entity.isAlive) {
            this.setVelocity(this.getVelocity().multiply(-0.05, 0.0, -0.05))
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
                    val vec3d = entity.rotationVector.normalize().multiply(2.5)
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

    override fun tick() {
        if (this.getDmg() in 20..29) {
            world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01),
                random.nextDouble().times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01)
            )
        }
        else if(this.getDmg() in 30..39){
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
            world.addParticle(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01),
                random.nextDouble().times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01)
            )
        }
        else if(this.getDmg() >= 40){
            world.addParticle(
                ParticleTypes.SOUL_FIRE_FLAME,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01),
                random.nextDouble().times(0.1),
                random.nextDouble().times(2).minus(1).times(0.01)
            )
            world.addParticle(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.1),
                random.nextDouble().times(0.2),
                random.nextDouble().times(2).minus(1).times(0.1)
            )
            world.addParticle(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                true,
                this.x + random.rangeInclusive(-1, 1).times(0.1),
                this.y + random.rangeInclusive(-1, 1).times(0.1),
                this.z + random.rangeInclusive(-1, 1).times(0.1),
                random.nextDouble().times(2).minus(1).times(0.1),
                random.nextDouble().times(0.2),
                random.nextDouble().times(2).minus(1).times(0.1)
            )
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if(this.getDmg() < 20){this.setDmg(20)}
        world.createExplosion(this, damageSources.explosion(this,this.owner), KnockbackExplosionBehavior(),this.x,this.y,this.z,this.getDmg().times(0.2).toFloat(),false,World.ExplosionSourceType.TNT)
        this.discard()
        super.onBlockHit(blockHitResult)
}

    fun setDmg(dmg: Int) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Int {
        return dataTracker.get(DMG) as Int
    }
}