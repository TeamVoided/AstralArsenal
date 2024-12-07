package org.teamvoided.astralarsenal.entity

import com.mojang.datafixers.optics.Lens
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World
import org.apache.logging.log4j.core.jmx.Server
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import javax.swing.Box

class ShockwaveEntity : Entity {

    constructor(entityType: EntityType<out ShockwaveEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.SHOCKWAVE as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    val BLOCK_RANGE = 1.0
    val BLOCK_WIDTH = 0.5

    var owner: Entity? = null
    var damage = 0.0f
    var knockback = 0.0
    var distance = 0.0
    var speed = 0.0

    var distance_yet = 0.0

    override fun tick() {
        if (this.distance_yet >= distance) {
            this.discard()
        }
        val entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                this, net.minecraft.util.math.Box(
                    this.pos.x + this.distance,
                    this.pos.y + this.BLOCK_WIDTH,
                    this.pos.z + this.distance,
                    this.pos.x - this.distance,
                    this.pos.y - this.BLOCK_WIDTH,
                    this.pos.z - this.distance
                )
            )
                .filter { it.distanceTo(this) > (distance_yet - BLOCK_RANGE) && it.distanceTo(this) < (distance_yet + BLOCK_RANGE) && it != this.owner }
        )
        if (entities.isNotEmpty()) {
            for (entity in entities) {
                if (this.damage > 0) {
                    entity.customDamage(AstralDamageTypes.PULVERISED, this.damage, this, this.owner)
                }
                if (this.knockback > 0) {
                    entity.addVelocity(0.0, this.knockback, 0.0)
                    entity.velocityDirty = true
                }
            }
        }
        val torusVolume =
            (2 * kotlin.math.PI * this.distance_yet) * (kotlin.math.PI * this.BLOCK_WIDTH * this.BLOCK_RANGE)
        for (i in 0..100) { //(torusVolume.times(0.1).toInt())) {
            world.addParticle(
                ParticleTypes.END_ROD,
                true,
                this.x + (world.random.nextInt(2).minus(0.5) * this.distance_yet * 2), //+ (world.random.nextInt(1).minus(0.5) * BLOCK_WIDTH * 2),
                this.y, // + (world.random.nextInt(2).minus(0.5) * this.BLOCK_WIDTH * 2),
                this.z + (world.random.nextInt(2).minus(0.5) * this.distance_yet * 2), // + (world.random.nextInt(1).minus(0.5) * BLOCK_WIDTH * 2),
                0.1,
                0.1,
                0.1
            )
            world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                true,
                this.x,
                this.y,
                this.z,
                0.1,
                0.1,
                0.1
            )
        }
        this.distance_yet += this.speed
        super.tick()
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

}