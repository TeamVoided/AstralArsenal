package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralSounds

class BeamOfLightEntity : Entity {

    constructor(entityType: EntityType<out BeamOfLightEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.BEAM_OF_LIGHT as EntityType<out Entity?>, world) {
        this.owner = owner
    }


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
    var targetEntity: Entity? = null
    var trackTime = 0
    var owner: Entity? = null
    var hard_damage = 0
    var enraged = false
    var outerColour = 0x00ffffff
    var innerColour = 0x00ffffff
    val weak = listOf(
        AstralEffects.BLEED
    )
    val nomove = listOf(
        AstralEffects.STATICALLY_SLUDGED
    )

    override fun tick() {
        if (this.owner != null && !this.owner!!.isAlive) {
            this.discard()
        }
        if (this.targetEntity != null && !this.targetEntity!!.isAlive) {
            this.targetEntity = null
        }
        incrementTime()
        val particles = if (enraged) ParticleTypes.GLOW else ParticleTypes.END_ROD
        if (this.getTime() < WINDUP) {
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    particles,
                    this.x,
                    this.y,
                    this.z,
                    4,
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    0.0,
                    random.nextDouble().minus(0.5).times(side).times(0.5),
                    0.0
                )
            }
            if (targetEntity != null && this.getTime() < trackTime) {
                val x = targetEntity!!.y + 1
                this.setPosition(targetEntity!!.pos.x, x, targetEntity!!.pos.z)
            } else if (targetEntity != null && this.getTime() == trackTime && enraged) {
                val x = targetEntity!!.y + 1
                val posx = targetEntity!!.x + (targetEntity!!.movement.x * ((WINDUP) - trackTime))
                val posy = (x)
                val posz = targetEntity!!.z + (targetEntity!!.movement.z * (WINDUP - trackTime))
                this.setPosition(posx, posy, posz)
            }
        } else if (this.getTime() == WINDUP) {
            this.playSound(AstralSounds.BEAM_BOOM, 1.0f, 1.0f)
            if (world is ServerWorld) {
                val beamRenderer = BeamRenderEntity(world, this.x, this.y + 1, this.z)
                beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, outerColour)
                beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, innerColour)
                beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, (this.TIMEACTIVE + 20))
                beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, (20))
                beamRenderer.dataTracker.set(
                    BeamRenderEntity.TargetPos,
                    Vector3f(this.x.toFloat(), this.y.toFloat() + 1000f, this.z.toFloat())
                )
                beamRenderer.dataTracker.set(
                    BeamRenderEntity.OriginPos,
                    Vector3f(this.x.toFloat(), -64f, this.z.toFloat())
                )
                beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, this.side.div(2).toFloat())
                beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, this.side.div(2).toFloat())
                beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, this.side.div(2).plus(1))
                beamRenderer.dataTracker.set(BeamRenderEntity.Opacity, 0.3f)
                beamRenderer.setPosition(this.x, this.y, this.z)
                world.spawnEntity(beamRenderer)
            }
        } else if (this.getTime() in WINDUP..(TIMEACTIVE + WINDUP)) {
            if (!DOT) {
                if (!world.isClient) {
                    if (this.getTime() % 5 == 0) {
                        this.playSound(AstralSounds.BEAM_VIBRATE, 2.0f, 1.0f)
                    }
                    val entities = world.getOtherEntities(
                        null, Box(
                            pos.x + side.times(0.5),
                            pos.y + 500.0,
                            pos.z + side.times(0.5),
                            pos.x + side.times(-0.5),
                            pos.y - 500,
                            pos.z + side.times(-0.5)
                        )
                    )
                    for (entity in entities) {
                        if (!entitiesHit.contains(entity) && (entity is LivingEntity || entity is CannonballEntity) && !entity.type.isIn(
                                AstralEntityTags.UNAFFECTED_BY_LIGHT
                            )
                        ) {
                            if (entity is CannonballEntity) {
                                entity.setCharged(true)
                                entity.owner = this.owner
                            } else {
                                if (entity is PlayerEntity) {
                                    entity.customDamage(
                                        AstralDamageTypes.BEAM_OF_LIGHT,
                                        this.DMG.toFloat(),
                                        this,
                                        owner
                                    )
                                } else {
                                    entity.customDamage(
                                        AstralDamageTypes.BEAM_OF_LIGHT,
                                        this.DMG.toFloat() * 3f,
                                        this,
                                        owner
                                    )
                                }
                                entity.addVelocity(0.0, THRUST, 0.0)
                                entitiesHit.add(entity)
                            }
                        }
                    }
                }
            } else {
                if (!world.isClient) {
                    if (this.getTime() % 5 == 0) {
                        this.playSound(AstralSounds.BEAM_VIBRATE, 2.0f, 1.0f)
                    }
                    val entities = world.getOtherEntities(
                        null, Box(
                            pos.x + side.times(0.5),
                            pos.y + 500.0,
                            pos.z + side.times(0.5),
                            pos.x + side.times(-0.5),
                            pos.y - 500,
                            pos.z + side.times(-0.5)
                        )
                    )
                    for (entity in entities) {
                        if (entity is LivingEntity) {
                            var hard_levels = 5
                            var duration = 2
                            val effects = entity.statusEffects.filter { weak.contains(it.effectType) }
                            if (effects.isNotEmpty()) {
                                effects.forEach {
                                    val w = it.amplifier
                                    hard_levels = w
                                    val u = kotlin.math.min(it.duration, 100)
                                    duration += u
                                }
                            }
                            entity.addStatusEffect(
                                StatusEffectInstance(
                                    AstralEffects.BLEED,
                                    duration, hard_levels,
                                    false, true, true
                                )
                            )
                            hard_levels = 0
                            duration = 2
                            val effects2 = entity.statusEffects.filter { nomove.contains(it.effectType) }
                            if (effects2.isNotEmpty()) {
                                effects2.forEach {
                                    val u = it.duration
                                    duration += u
                                }
                            }
                            entity.addStatusEffect(
                                StatusEffectInstance(
                                    AstralEffects.STATICALLY_SLUDGED,
                                    duration, hard_levels,
                                    false, false, true
                                )
                            )
                        }
                    }
                }
            }
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    this.x,
                    this.y,
                    this.z,
                    10,
                    random.nextDouble().minus(0.5).times(side),
                    random.nextDouble().minus(0.5).times(200),
                    random.nextDouble().minus(0.5).times(side),
                    0.02
                )
            }
        } else if (this.getTime() > (TIMEACTIVE + WINDUP)) {
            this.playSound(AstralSounds.BEAM_WIND, 3.0f, 1.0f)
            this.discard()
        }
        if (this.getTime() in trackTime..WINDUP) {
            val times = (this.getTime() - trackTime) + 1
            if (!world.isClient) {
                for (i in 0..times) {
                    val serverWorld = world as ServerWorld
                    serverWorld.spawnParticles(
                        ParticleTypes.CRIT,
                        this.x,
                        this.y + (i * 0.3),
                        this.z,
                        3,
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
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    fun setTime(time: Int) {
        dataTracker.set(TIME, time)
    }

    fun incrementTime() = setTime(getTime() + 1)

    fun getTime(): Int {
        return dataTracker.get(TIME) as Int
    }
}