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
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralSounds

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

    var WINDUP = 1
    var TIMEACTIVE = 1
    var DOT = false
    var THRUST = 2.0
    var DMG = 1
    var side = 1
    var entitiesHit = mutableListOf<Entity>()
    var targetEntity : Entity? = null
    var trackTime = 0

    override fun tick() {
        incrementTime()
        if (this.getTime() < WINDUP) {
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    this.x,
                    this.y,
                    this.z,
                    10,
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    0.0,
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    0.0
                )
            }
            if (targetEntity != null && this.getTime() < trackTime){
                this.setPosition(targetEntity!!.pos.x, targetEntity!!.pos.y + 1, targetEntity!!.pos.z)
            }
        }
        else if(this.getTime() == WINDUP){this.playSound(AstralSounds.BEAM_BOOM, 1.0f, 1.0f)}
        else if(this.getTime() in WINDUP..(TIMEACTIVE+WINDUP)){
            if (!DOT){
            if (!world.isClient) {
                if(this.getTime() % 5 == 0){this.playSound(AstralSounds.BEAM_VIBRATE,2.0f,1.0f)}
                val serverWorld = world as ServerWorld
                for(i in 0..100){
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    this.x,
                    (this.y + (i * 0.5)) - 5,
                    this.z,
                    3,
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    random.nextDouble().minus(0.5).times(1),
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    0.0
                )}
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
                    entity.customDamage(AstralDamageTypes.BEAM_OF_LIGHT,this.DMG.toFloat())
                    entity.addVelocity(0.0,THRUST,0.0)
                        entitiesHit.add(entity)
                    }
                }}
            }
            else{if (!world.isClient) {
                if(this.getTime() % 5 == 0){this.playSound(AstralSounds.BEAM_VIBRATE,2.0f,1.0f)}
                val serverWorld = world as ServerWorld
                for(i in 0..100){
                    serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        this.x,
                        (this.y + (i * 0.5)) - 5,
                        this.z,
                        10,
                        random.nextDouble().minus(0.5).times(side).times(0.5),
                        random.nextDouble().minus(0.5).times(1),
                        random.nextDouble().minus(0.5).times(side).times(0.5),
                        0.0
                    )}
                val entities = world.getOtherEntities(
                    null, Box(
                        pos.x + side.times(0.5),
                        pos.y + 10.0,
                        pos.z + side.times(0.5),
                        pos.x + side.times(-0.5),
                        pos.y - 10,
                        pos.z + side.times(-0.5)
                    ))
                for (entity in entities) {
                        entity.customDamage(AstralDamageTypes.BEAM_OF_LIGHT,this.DMG.toFloat())
                    }
                }}
        }
        else if (this.getTime() > (TIMEACTIVE + WINDUP)){
            this.playSound(AstralSounds.BEAM_WIND, 3.0f, 1.0f)
            this.discard()
        }
        if (this.getTime() in trackTime..WINDUP){
            val times = (this.getTime() - trackTime) + 1
            if (!world.isClient) {
                for(i in 0..times) {
                    val serverWorld = world as ServerWorld
                    serverWorld.spawnParticles(
                        ParticleTypes.CRIT,
                        this.x,
                        this.y + (i * 0.3),
                        this.z,
                        10,
                        random.nextDouble().minus(0.5).times(side).times(0.5),
                        0.0,
                        random.nextDouble().minus(0.5).times(side).times(0.5),
                        0.0
                    )
                }
            }
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