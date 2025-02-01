package org.teamvoided.astralarsenal.entity.astralenemies.goals

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.entity.astralenemies.AstralSniperEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.world.explosion.*
import kotlin.math.*

class SnipeGoal(val entity: AstralSniperEntity) : Goal() {
    override fun canStart(): Boolean {
        return (entity.target != null && entity.distanceTo(entity.target) <= 75)
    }

    override fun stop() {
        entity.enraged = false
        entity.snipesOnTarget = 0
        entity.timeBeforeShot = 80
        entity.shotBufferTime = 20
        entity.isShooting = false
        entity.cantMove = false
        super.stop()
    }

    override fun shouldContinue(): Boolean {
        return (entity.target != null && entity.distanceTo(entity.target) <= 75)
    }

    override fun tick() {
        if (entity.cooldown > 0) {
            entity.cooldown--
            entity.isShooting = false
            entity.targetPoint = entity.target!!.pos.add(0.0, 1.0, 0.0)
            entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.targetPoint)
            entity.cantMove = false
        } else if (entity.timeBeforeShot > 0) {
            entity.timeBeforeShot--
            entity.isShooting = true
            entity.cantMove = true
            entity.targetPoint = entity.target!!.pos.add(0.0, 1.0, 0.0)
            var particle = ParticleTypes.FLAME
            if (entity.timeBeforeShot % 10 == 0) {
                entity.world.playSoundFromEntity(
                    null,
                    entity.target!!,
                    SoundEvents.BLOCK_SAND_FALL,
                    SoundCategory.HOSTILE,
                    1.0f,
                    1.5f
                )
                particle = ParticleTypes.SOUL_FIRE_FLAME
            }
            showTarget(entity, particle)
            if (!entity.canSee(entity.target)) {
                entity.cooldown += 20
                entity.timeBeforeShot = min(entity.timeBeforeShot + 20, 50)
            }
        } else if (entity.shotBufferTime > 0) {
            if ((entity.shotBufferTime == 20 || (entity.enraged && entity.shotBufferTime == 10))) {
                entity.targetPoint = entity.target!!.pos.add(0.0, 1.0, 0.0)
                if (entity.enraged) {
                    entity.targetPoint = entity.target!!.pos.add(0.0, 1.0, 0.0)
                        .add(
                            entity.target!!.movement.x * (entity.shotBufferTime.toDouble().times(2.0)),
                            0.0,
                            entity.target!!.movement.z * (entity.shotBufferTime.toDouble().times(2.0))
                        )
                }
                entity.world.playSoundFromEntity(
                    null,
                    entity.target!!,
                    SoundEvents.BLOCK_TRIAL_SPAWNER_DETECT_PLAYER,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.5f
                )
                if (!entity.canSee(entity.target)) {
                    entity.cooldown += 20
                    entity.timeBeforeShot += 20
                    entity.shotBufferTime = if (entity.enraged) 11 else 21
                }
            }
            entity.shotBufferTime--
            showTarget(entity, ParticleTypes.SOUL_FIRE_FLAME)
        } else if (entity.shotBufferTime == 0) {
            if (entity.snipeType == AstralSniperEntity.SnipeType.EXPLOSIVE) explosiveBeam(entity)
            else laserBeam(entity)
            if (entity.snipeType == AstralSniperEntity.SnipeType.DOUBLE && !entity.takenSecondShot) {
                entity.shotBufferTime = if (entity.enraged) 10 else 20
                entity.takenSecondShot = true
            } else {
                entity.takenSecondShot = false
                entity.timeBeforeShot = 80
                entity.shotBufferTime = if (entity.enraged) 10 else 20
                entity.cooldown = if (entity.enraged) 50 else 100
                entity.snipesOnTarget++
                if (entity.snipesOnTarget > entity.getShotsBeforeEnrage(entity.world)) {
                    entity.enraged = true
                }
            }
        }
        super.tick()
    }

    fun laserBeam(entity: AstralSniperEntity) {
        entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.targetPoint)
        val result = this.entity.raycast(100.0, 1f, false)
        val world = entity.world
        val distance = sqrt(
            sqrt((entity.eyePos.x - result.pos.x).pow(2) + (entity.eyePos.z - result.pos.z).pow(2)).pow(2) + ((entity.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    entity, Box(
                        (lerp(entity.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)) + 0.5,
                        (lerp(entity.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(entity.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)) - 0.5,
                        (lerp(entity.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                )
            )
            if (!entity.world.isClient) {
                val serverWorld = entity.world as ServerWorld
                val particle = when (entity.snipeType) {
                    AstralSniperEntity.SnipeType.RANCID -> ParticleTypes.ENCHANT
                    AstralSniperEntity.SnipeType.ICY -> ParticleTypes.SNOWFLAKE
                    else -> ParticleTypes.END_ROD
                }
                serverWorld.spawnParticles(
                    particle,
                    (lerp(entity.eyePos.x, result.pos.x, i / interval)),
                    (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (lerp(entity.eyePos.z, result.pos.z, i / interval)),
                    10,
                    0.2,
                    0.2,
                    0.2,
                    0.0
                )

            }
        }
        world.playSound(
            null,
            entity.x,
            entity.y,
            entity.z,
            AstralSounds.RAILGUN,
            SoundCategory.PLAYERS,
            50.0F,
            1.0f
        )
        for (victim in entities) {
            if (victim is CannonballEntity) {
                val explosion = when (entity.snipeType) {
                    AstralSniperEntity.SnipeType.SINGLE -> StrongExplosionBehavior(entity)
                    AstralSniperEntity.SnipeType.DOUBLE -> WeakExplosionBehavior(entity)
                    AstralSniperEntity.SnipeType.RANCID -> RancidExplosionBehavior()
                    AstralSniperEntity.SnipeType.ICY -> FrostExplosionBehavior()
                    else -> KnockbackExplosionBehavior(entity)
                }
                world.createExplosion(
                    victim,
                    victim.damageSources.explosion(victim, entity),
                    explosion,
                    victim.x,
                    victim.y,
                    victim.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                entity.discard()
            }
            if (victim is BeamOfLightEntity && victim.targetEntity != null && victim.targetEntity == entity) {
                world.createExplosion(
                    victim,
                    victim.damageSources.explosion(victim, entity),
                    PenopticonExplosionBehavior(entity),
                    victim.x,
                    victim.y,
                    victim.z,
                    1.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                break
            }
            if (victim is LivingEntity) {
                when (entity.snipeType) {
                    AstralSniperEntity.SnipeType.SINGLE -> {
                        victim.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                                entity,
                                entity
                            ), 10f
                        )
                    }

                    AstralSniperEntity.SnipeType.DOUBLE -> {
                        victim.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RAILED),
                                entity,
                                entity
                            ), 7.5f
                        )
                    }

                    AstralSniperEntity.SnipeType.ICY -> {
                        victim.damage(
                            DamageSource(
                                AstralDamageTypes.getHolder(world.registryManager, DamageTypes.FREEZE),
                                entity,
                                entity
                            ), 10f
                        )
                        victim.frozenTicks = 200
                    }

                    AstralSniperEntity.SnipeType.RANCID -> {
                        victim.addStatusEffect(
                            StatusEffectInstance(
                                StatusEffects.SLOWNESS,
                                300, 0,
                                false, true, true
                            )
                        )
                        victim.addStatusEffect(
                            StatusEffectInstance(
                                StatusEffects.POISON,
                                300, 0,
                                false, true, true
                            )
                        )
                        victim.addStatusEffect(
                            StatusEffectInstance(
                                StatusEffects.WEAKNESS,
                                300, 0,
                                false, true, true
                            )
                        )
                        victim.addStatusEffect(
                            StatusEffectInstance(
                                AstralEffects.REDUCE,
                                300, 9,
                                false, true, true
                            )
                        )
                        victim.addStatusEffect(
                            StatusEffectInstance(
                                AstralEffects.BLEED,
                                300, 0,
                                false, true, true
                            )
                        )
                    }

                    else -> println("Error in SnipeGoal, Report problem to Astra - error code 1")
                }
            }
        }
    }

    fun explosiveBeam(entity: AstralSniperEntity) {
        entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.targetPoint)
        val result = entity.raycast(100.0, 1f, false)
        val world = entity.world
        val distance = sqrt(
            sqrt((entity.eyePos.x - result.pos.x).pow(2) + (entity.eyePos.z - result.pos.z).pow(2)).pow(2) + ((entity.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    entity, Box(
                        (lerp(entity.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)) + 0.5,
                        (lerp(entity.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (lerp(entity.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)) - 0.5,
                        (lerp(entity.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                ).filter { it !is ProjectileEntity }
            )
            if (entities.isNotEmpty()) {
                break
            }
            if (!entity.world.isClient) {
                val serverWorld = entity.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    (lerp(entity.eyePos.x, result.pos.x, i / interval)),
                    (lerp(entity.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (lerp(entity.eyePos.z, result.pos.z, i / interval)),
                    5,
                    0.2,
                    0.2,
                    0.2,
                    0.0
                )

            }
        }
        world.playSound(
            null,
            entity.x,
            entity.y,
            entity.z,
            AstralSounds.RAILGUN,
            SoundCategory.PLAYERS,
            50.0F,
            1.0f
        )
        for (victim in entities) {
            if (victim is CannonballEntity) {
                world.createExplosion(
                    victim,
                    victim.damageSources.explosion(victim, entity),
                    PenopticonExplosionBehavior(entity),
                    victim.x,
                    victim.y,
                    victim.z,
                    3.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                victim.discard()
                break
            } else if (victim is BeamOfLightEntity && victim.targetEntity != null && victim.targetEntity == entity) {
                world.createExplosion(
                    victim,
                    victim.damageSources.explosion(victim, entity),
                    PenopticonExplosionBehavior(entity),
                    victim.x,
                    victim.y,
                    victim.z,
                    1.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                break
            } else {
                world.createExplosion(
                    null,
                    victim.damageSources.explosion(null, entity),
                    StrongExplosionBehavior(entity),
                    victim.x,
                    victim.y,
                    victim.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                if (!entity.world.isClient) {
                    val serverWorld = entity.world as ServerWorld
                    serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        victim.x,
                        victim.y,
                        victim.z,
                        500,
                        1.5,
                        1.5,
                        1.5,
                        0.0
                    )
                }
                break
            }
        }
        if (entities.isEmpty()) {
            world.createExplosion(
                null,
                entity.damageSources.explosion(null, entity),
                StrongExplosionBehavior(entity),
                result.pos.x,
                result.pos.y,
                result.pos.z,
                2.0f,
                false,
                World.ExplosionSourceType.TNT
            )
            if (!entity.world.isClient) {
                val serverWorld = entity.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    result.pos.x,
                    result.pos.y,
                    result.pos.z,
                    500,
                    1.5,
                    1.5,
                    1.5,
                    0.0
                )
            }
        }
    }

    fun showTarget(entity: AstralSniperEntity, particle: ParticleEffect) {
        entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.targetPoint)
        val distance = sqrt(
            sqrt((entity.eyePos.x - entity.targetPoint!!.x).pow(2) + (entity.eyePos.z - entity.targetPoint!!.z).pow(2)).pow(
                2
            ) + ((entity.eyePos.y - 0.5) - entity.targetPoint!!.y).pow(
                2
            )
        )
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            if (entity.world is ServerWorld) {
                val serverWorld = entity.world as ServerWorld
                serverWorld.spawnParticles(
                    particle,
                    (lerp(entity.eyePos.x, entity.targetPoint!!.x, i / interval)),
                    (lerp(entity.eyePos.y, entity.targetPoint!!.y, i / interval)),
                    (lerp(entity.eyePos.z, entity.targetPoint!!.z, i / interval)),
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    fun findTargetSpot(entity: AstralSniperEntity) {
        if (entity.target != null && shouldMove(entity, 50f, 10f)) {
            var r = 0f
            val distance = entity.distanceTo(entity.target)
            if (distance > 50f) {
                r = distance - 45
            } else if (distance < 10f) {
                r = distance + 15
            }
            val targX = entity.target!!.x
            val targZ = entity.target!!.z
            val posX = entity.x
            val posZ = entity.z
            val theta = atan2((targZ - posZ), (targX - posX))

        }
    }

    fun shouldMove(entity: AstralSniperEntity, maxDistance: Float, minDistance: Float): Boolean {
        return !(entity.target != null && entity.distanceTo(entity.target) > minDistance && entity.distanceTo(entity.target) < maxDistance)
    }
}