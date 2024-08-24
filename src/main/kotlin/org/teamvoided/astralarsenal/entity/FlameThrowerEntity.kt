package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities

class FlameThrowerEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out FlameThrowerEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.FLAME_THROWER_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.FLAME_THROWER_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.COD
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (entityHitResult.entity != owner && entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity
            hit.customDamage(AstralDamageTypes.BURN, 1f, this, owner)
            hit.setOnFireFor(100)
        }
    }

    override fun tick() {
        world.addParticle(
            ParticleTypes.SOUL_FIRE_FLAME,
            true,
            this.x + random.rangeInclusive(-1, 1).times(0.1),
            this.y + random.rangeInclusive(-1, 1).times(0.1),
            this.z + random.rangeInclusive(-1, 1).times(0.1),
            this.velocity.x.times(0.2) + random.nextDouble().times(2).minus(1).times(0.01),
            this.velocity.x.times(0.2) + random.nextDouble().times(0.1),
            this.velocity.z.times(0.2) + random.nextDouble().times(2).minus(1).times(0.01)
        )
        super.tick()
        if (this.isWet) {
            this.discard()
        }
        this.addVelocity(0.0, 0.01, 0.0)
        this.setTime(this.getTime() + 1)
        if (this.getTime() >= 20) {
            this.discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    init {
        this.setNoGravity(true)
    }

    companion object {
        private val time: TrackedData<Int> =
            DataTracker.registerData(FlameThrowerEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(time, 0)
    }

    fun setTime(Time: Int) {
        dataTracker.set(FlameThrowerEntity.time, Time)
    }

    fun getTime(): Int {
        return dataTracker.get(FlameThrowerEntity.time) as Int
    }
}