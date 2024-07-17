package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities

class BeamOfLightEntity : Entity {

    constructor(entityType: EntityType<out BeamOfLightEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.BEAM_OF_LIGHT as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.BEAM_OF_LIGHT as EntityType<out Entity?>, world)

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(TIME, 0)
    }

    companion object {
        private val TIME: TrackedData<Int>? =
            DataTracker.registerData(BeamOfLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    var WINDUP = 20
    var TIMEACTIVE = 20
    var DOT = false
    var THRUST = 2.0
    var DMG = 10
    var side = 1
    var entitiesHit = mutableListOf<Entity>()

    override fun tick() {
        incrementTime()
        if (this.getTime() < WINDUP) {
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    this.x,
                    this.y,
                    this.z,
                    10,
                    random.nextDouble().minus(0.5).times(2).times(side),
                    0.0,
                    random.nextDouble().minus(0.5).times(2).times(side),
                    0.0
                )
            }
        }
        else if(this.getTime() == WINDUP){this.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f)}
        else if(this.getTime() in WINDUP..(TIMEACTIVE+WINDUP)){
            if (!DOT){
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    this.x,
                    this.y,
                    this.z,
                    100,
                    random.nextDouble().minus(0.5).times(2).times(side),
                    random.nextDouble().minus(0.5).times(100),
                    random.nextDouble().minus(0.5).times(2).times(side),
                    0.0
                )
                val entities = world.getOtherEntities(
                    null, Box(
                        pos.x + side.times(0.5),
                        pos.y + 50.0,
                        pos.z + side.times(0.5),
                        pos.x + side.times(-0.5),
                        pos.y - 50,
                        pos.z + side.times(-0.5)
                    ))
                for (entity in entities) {
                    if(!entitiesHit.contains(entity)){
                    entity.customDamage(AstralDamageTypes.CANNONBALL,this.DMG.toFloat())
                    entity.addVelocity(0.0,THRUST,0.0)
                        entitiesHit.add(entity)
                    }
                }}
            }
            else{if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    side.times(0.5),
                    100.0,
                    side.times(0.5),
                    10000000,
                    0.0,
                    0.0,
                    0.0,
                    0.5
                )
                val entities = world.getOtherEntities(
                    null, Box(
                        pos.x + side.times(0.5),
                        pos.y + 50.0,
                        pos.z + side.times(0.5),
                        pos.x + side.times(-0.5),
                        pos.y - 50,
                        pos.z + side.times(-0.5)
                    ))
                for (entity in entities) {
                        entity.customDamage(AstralDamageTypes.CANNONBALL,this.DMG.toFloat())
                    }
                }}
        }
        else if (this.getTime() > (TIMEACTIVE + WINDUP)){
            this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f)
            this.discard()
        }
        super.tick()
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