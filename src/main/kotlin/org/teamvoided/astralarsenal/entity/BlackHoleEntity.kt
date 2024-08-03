package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity.Companion
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import kotlin.math.pow
import kotlin.math.sqrt

class BlackHoleEntity : Entity {

    constructor(entityType: EntityType<out BlackHoleEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.BLACK_HOLE_ENTITY as EntityType<out Entity?>, world) {
    }

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BLACK_HOLE_ENTITY as EntityType<out Entity?>, world)

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(TIME, 0)
    }

    companion object {
        private val TIME: TrackedData<Int>? =
            DataTracker.registerData(BlackHoleEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    init{ this.setNoGravity(true) }

    var target : LivingEntity? = null
    var owner : LivingEntity? = null

    override fun tick() {
        if(this.target != null || this.owner != null){
            val targetpos = target!!.pos
            val thispos = this.pos
            val distance = sqrt(sqrt((thispos.x - targetpos.x).pow(2) + (thispos.z - targetpos.z).pow(2)).pow(2) + ((thispos.y - 0.5) - targetpos.y).pow(2))
            val interv = (distance * 10)
            this.setPosition(
                (lerp(thispos.x, targetpos.x, 1 / interv)),
                (lerp(thispos.y, targetpos.y, 1 / interv)),
                (lerp(thispos.z, targetpos.z, 1 / interv))
            )
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.DRAGON_BREATH,
                    this.x,
                    this.y,
                    this.z,
                    100,
                    1.0,
                    1.0,
                    1.0,
                    0.0
                )
            }
            val entities = world.getOtherEntities(
                null, Box(
                    pos.x + 1.5,
                    pos.y + 1.5,
                    pos.z + 1.5,
                    pos.x - 1.5,
                    pos.y - 1.5,
                    pos.z - 1.5
                )
            )
            for (entity in entities) {
                if(entity is LivingEntity){
                    val hp = entity.health * 0.5
                    val maxhp = entity.maxHealth
                    entity.customDamage(AstralDamageTypes.BEAM_OF_LIGHT, hp.toFloat(), this, this.owner)
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            AstralEffects.UNHEALABLE_DAMAGE,
                            100, (maxhp).toInt(),
                            false, true, true)
                        )
                }
                if(entity == this.target){
                    this.discard()
                }
            }
        }
        this.incrementTime()
        if(this.getTime() > 1200){this.discard()}
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
        //TODO("Not yet implemented")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
        //TODO("Not yet implemented")
    }
    fun setTime(time: Int) {
        dataTracker.set(TIME, time)
    }

    fun incrementTime() = setTime(getTime() + 1)

    fun getTime(): Int {
        return dataTracker.get(TIME) as Int
    }
}