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
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEntities

class SlashEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out SlashEntity?>?, world: World?) :
            super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SLASH_ENTITY as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.SLASH_ENTITY as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override fun getDefaultItem(): Item {
        return Items.COD
    }

    companion object {
        private val DMG: TrackedData<Float> =
            DataTracker.registerData(SlashEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        private val time: TrackedData<Int> =
            DataTracker.registerData(SlashEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        // Note: this is not where to change the damage value. Change it in the kosmogliph.
        builder.add(DMG, 0f)
        builder.add(time, 0)
    }

    init {
        this.setNoGravity(true)
    }

    override fun tick() {
        this.setTime(this.getTime() + 1)
        if (this.getTime() > 7) {
            this.discard()
        }
        if (!this.world.isClient) {
            val serverWorld = this.world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.ENCHANT,
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
        if (entityHitResult.entity is PlayerEntity) {
            entityHitResult.entity.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(world.registryManager, DamageTypes.MAGIC),
                    this,
                    owner
                ), this.getDmg()
            )
        } else {
            entityHitResult.entity.damage(
                DamageSource(
                    AstralDamageTypes.getHolder(world.registryManager, DamageTypes.MAGIC),
                    this,
                    owner
                ), this.getDmg().times(2.5).toFloat()
            )
        }
        super.onEntityHit(entityHitResult)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    fun setDmg(dmg: Float) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Float {
        return dataTracker.get(DMG) as Float
    }

    fun setTime(Time: Int) {
        dataTracker.set(time, Time)
    }

    fun getTime(): Int {
        return dataTracker.get(time) as Int
    }
}