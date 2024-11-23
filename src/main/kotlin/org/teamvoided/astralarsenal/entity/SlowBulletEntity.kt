package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEntities

class SlowBulletEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out SlowBulletEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SLOW_BULLET as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.SLOW_BULLET as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.COD
    }

    companion object {
        private val DMG: TrackedData<Float> =
            DataTracker.registerData(SlowBulletEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        // Note: this is not where to change the damage value. Change it in where its summoned.
        builder.add(DMG, 0f)
    }

    init {
        this.setNoGravity(true)
    }

    override fun tick() {
        if(this.velocity == Vec3d(0.0,0.0,0.0)){
            this.discard()
        }
        if (!this.world.isClient) {
            val serverWorld = this.world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.DRAGON_BREATH,
                this.x,
                this.y,
                this.z,
                1,
                this.world.random.nextDouble().minus(0.5).times(0.5),
                0.0,
                this.world.random.nextDouble().minus(0.5).times(0.5),
                0.0
            )
            super.tick()
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        entityHitResult.entity.damage(
            DamageSource(
                AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.CANNONBALL),
                this,
                owner
            ), this.getDmg()
        )
        super.onEntityHit(entityHitResult)
        this.discard()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    override fun updateVelocity(speed: Float, movementInput: Vec3d?) {
        super.updateVelocity(speed, movementInput)
    }

    fun setDmg(dmg: Float) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Float {
        return dataTracker.get(DMG) as Float
    }
}