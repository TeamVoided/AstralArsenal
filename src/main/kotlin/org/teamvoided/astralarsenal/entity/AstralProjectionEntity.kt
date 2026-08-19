package org.teamvoided.astralarsenal.entity

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Math
import org.joml.Vector3f
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class AstralProjectionEntity : Entity {
    var damage = 3
    var owner: LivingEntity? = null
    var countdown: Int? = null

    constructor(entityType: EntityType<out AstralProjectionEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.ASTRAL_PROJECTION as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    override fun tick() {
        if (this.owner == null && !this.world.isClient){
            this.discard()
        }
        if (countdown != null) {
            this.countdown = this.countdown!! - 1
            if (countdown!! > 5) {
                val entities = world.getOtherEntities(
                    null, Box(
                        pos.x + 10,
                        pos.y + 10,
                        pos.z + 10,
                        pos.x - 10,
                        pos.y - 10,
                        pos.z - 10
                    )
                ).filter { it != this.owner && it is LivingEntity }
                var closestEntity: LivingEntity? = null
                for (entity in entities) {
                    if (closestEntity != null) {
                        val e1d = this.distanceTo(closestEntity)
                        val e2d = this.distanceTo(entity)
                        if (e2d < e1d) {
                            closestEntity = entity as LivingEntity
                        }
                    } else {
                        closestEntity = entity as LivingEntity
                    }
                }
                if (closestEntity != null) {
                    this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, closestEntity.eyePos)

                }
            }
            if (countdown!! < 1) {
                if (this.owner is PlayerEntity) {
                    rail(this.world, this.owner as PlayerEntity, this)
                    if (this.world is ServerWorld) {
                        world.playSound(
                            null,
                            this.x,
                            this.y,
                            this.z,
                            SoundEvents.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM,
                            SoundCategory.PLAYERS,
                            1.0F,
                            2.0f
                        )
                    }
                }
                this.discard()
            }
        }
        super.tick()
    }

    override fun hasNoGravity(): Boolean {
        return true
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    fun rail(world: World, cause: PlayerEntity, caster: AstralProjectionEntity): Int {
        val entitiesHit = mutableListOf<Entity>()
        entitiesHit.add(cause)
        val combined = caster.eyePos.add(caster.rotationVector.multiply(100.0))
        val result = world.raycast(
            RaycastContext(
                caster.eyePos, combined, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, caster
            )
        )
        val distance = sqrt(
            sqrt((caster.eyePos.x - result.pos.x).pow(2) + (caster.eyePos.z - result.pos.z).pow(2)).pow(2) + ((caster.eyePos.y - 0.5) - result.pos.y).pow(
                2
            )
        )
        if (world is ServerWorld) {
            val beamRenderer = BeamRenderEntity(world, caster.x, caster.y + 1, caster.z)
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, 0x7d34ebb.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, 0x00000000.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, 6)
            beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.TargetPos, result.pos.toVector3f())
            beamRenderer.dataTracker.set(
                BeamRenderEntity.OriginPos,
                Vector3f(caster.x.toFloat(), (caster.eyePos.y).toFloat(), caster.z.toFloat())
            )
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, 0.2f)
            beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, 0.2f)
            beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, 2)
            beamRenderer.setPosition(caster.eyePos.x, caster.eyePos.y, caster.eyePos.z)
            world.spawnEntity(beamRenderer)
        }
        val entities = mutableListOf<Entity>()
        val interval = (distance.times(2))
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    caster, Box(
                        (Math.lerp(caster.eyePos.x, result.pos.x, i / interval)) + 0.5,
                        (Math.lerp(caster.eyePos.y - 0.5, result.pos.y, i / interval)) + 0.5,
                        (Math.lerp(caster.eyePos.z, result.pos.z, i / interval)) + 0.5,
                        (Math.lerp(caster.eyePos.x, result.pos.x, i / interval)) - 0.5,
                        (Math.lerp(caster.eyePos.y - 0.5, result.pos.y, i / interval)) - 0.5,
                        (Math.lerp(caster.eyePos.z, result.pos.z, i / interval)) - 0.5
                    )
                )
            )
            if (!caster.world.isClient && world.random.nextInt(1) == 0) {
                val serverWorld = caster.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    (Math.lerp(caster.eyePos.x, result.pos.x, i / interval)),
                    (Math.lerp(caster.eyePos.y - 0.5, result.pos.y, i / interval)),
                    (Math.lerp(caster.eyePos.z, result.pos.z, i / interval)),
                    1,
                    0.2,
                    0.2,
                    0.2,
                    0.0
                )

            }
        }
        for (entity in entities) {
            if (entity is CannonballEntity && !entitiesHit.contains(entity)) {
                world.createExplosion(
                    entity,
                    entity.damageSources.explosion(entity, cause),
                    WeakExplosionBehavior(cause),
                    entity.x,
                    entity.y,
                    entity.z,
                    2.0f,
                    false,
                    World.ExplosionSourceType.TNT
                )
                entity.discard()
            }
            if (entity is LivingEntity && !entitiesHit.contains(entity)
            ) {
                val damage = if (entity is PlayerEntity) damage.toFloat() else damage.toFloat() * 2
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, DamageTypes.MAGIC),
                        cause,
                        cause
                    ), damage
                )
            }
            entitiesHit.add(entity)
        }
        return 0
    }

}