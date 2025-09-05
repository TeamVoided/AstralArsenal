package org.teamvoided.astralarsenal.entity.entitiesThatAreJustHereCauseOtherShitDontWork

import net.minecraft.block.ShapeContext
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Math
import org.joml.Vector3f
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class RichochetEntity : Entity {
    var owner: PlayerEntity? = null
    var dmg = 0.0
    var cooldown = 0.1
    var COUNTDOWN = 0

    constructor(entityType: EntityType<out RichochetEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.RICHOCHET as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.RICHOCHET as EntityType<out Entity?>, world)

    override fun tick() {
        if (this.COUNTDOWN <= 0) this.discard()
        else {
            if (this.cooldown > 0) {
                this.cooldown--
                val entities = mutableListOf<Entity>()
                entities.addAll(
                    world.getOtherEntities(
                        this,
                        Box(
                            Vec3d(this.x + 10, this.y + 10, this.z + 10),
                            Vec3d(this.x - 10, this.y - 10, this.z - 10)
                        )
                    )
                        .filter { !hitTwice.contains(it) && !hitThrice.contains(it) && it != owner && ((it is LivingEntity && !it.isDead)|| it is CannonballEntity) }
                )
                if (entities.isNotEmpty()) {
                    repeat((entities.size - 1)) {
                        entities.removeAt(this.world.random.rangeInclusive(0, entities.size - 1))
                    }
                    if (blockedByBlocks(this.pos, entities.first().pos)) {
                        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entities.first().eyePos)
                    }
                }
            } else {
                if (this.owner != null) {
                    rail(world, this.owner!!, this)
                }
                world.playSound(
                    null,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.BLOCK_HEAVY_CORE_BREAK,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
                this.discard()
            }
        }
        super.tick()
    }

    fun blockedByBlocks(pos1: Vec3d, pos2: Vec3d): (Boolean) {
        val distance1 = pos1.distanceTo(pos2)
        val distance2 = pos1.distanceTo(
            world.raycast(
                RaycastContext(
                    pos1,
                    pos2,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    ShapeContext.absent()
                )
            ).pos
        )
        return (distance1 == distance2)
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    val hitOnce = mutableListOf<Entity>()
    val hitTwice = mutableListOf<Entity>()
    val hitThrice = mutableListOf<Entity>()

    fun rail(world: World, cause: PlayerEntity, caster: RichochetEntity): Int {
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
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.OuterColour, 0x00757575.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.InterColour, 0x00ffd700.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.LiveTime, 6)
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.TargetPos, result.pos.toVector3f())
            beamRenderer.dataTracker.set(
                BeamRenderEntity.Companion.OriginPos,
                Vector3f(caster.x.toFloat(), (caster.eyePos.y).toFloat(), caster.z.toFloat())
            )
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.OuterThickness, 0.5f)
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.MaxOuterThickness, 0.5f)
            beamRenderer.dataTracker.set(BeamRenderEntity.Companion.InnerCubes, 4)
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
            if (entity is LivingEntity && !entitiesHit.contains(entity) && (entity !is PlayerEntity || !hitThrice.contains(
                    entity
                ))
            ) {
                val damage = if (entity is PlayerEntity) dmg.toFloat() else dmg.toFloat() * 2
                entity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(world.registryManager, AstralDamageTypes.RICHOCHET),
                        cause,
                        cause
                    ), damage
                )
                if (entity is LivingEntity/*PlayerEntity*/) {
                    when {
                        hitThrice.contains(entity) -> {}
                        hitTwice.contains(entity) -> {
                            hitThrice.add(entity); hitTwice.remove(entity)
                        }

                        hitOnce.contains(entity) -> {
                            hitTwice.add(entity); hitOnce.remove(entity)
                        }

                        else -> hitOnce.add(entity)
                    }
                }
            }
            entitiesHit.add(entity)
        }
        if (result.side != null && result.type != HitResult.Type.MISS) {
            val richochet = RichochetEntity(world, cause)
            richochet.pitch = caster.pitch
            richochet.yaw = caster.yaw
            richochet.COUNTDOWN = COUNTDOWN - 1
            richochet.dmg = dmg
            richochet.owner = cause
            richochet.hitOnce.addAll(hitOnce)
            richochet.hitTwice.addAll(hitTwice)
            richochet.hitThrice.addAll(hitThrice)
            val y = richochet.yaw
            when (result.side) {
                Direction.DOWN, Direction.UP -> {
                    richochet.pitch *= -1
                    if (result.side == Direction.UP) richochet.setPosition(
                        result.pos.x,
                        result.pos.y + 0.1,
                        result.pos.z
                    )
                    else richochet.setPosition(result.pos.x, result.pos.y - 0.1, result.pos.z)
                }

                Direction.SOUTH -> {
                    if (richochet.yaw >= 0) richochet.yaw = ((180) - y)
                    else richochet.yaw = ((-180) - y)
                    richochet.setPosition(result.pos.x, result.pos.y, result.pos.z + 0.1)
                }

                Direction.NORTH -> {
                    if (richochet.yaw > 0) richochet.yaw = ((180) - y)
                    else richochet.yaw = ((-180) - y)
                    richochet.setPosition(result.pos.x, result.pos.y, result.pos.z - 0.1)
                }

                Direction.WEST -> {
                    richochet.yaw = y * -1
                    richochet.setPosition(result.pos.x - 0.1, result.pos.y, result.pos.z)
                }

                Direction.EAST -> {
                    richochet.yaw = y * -1
                    richochet.setPosition(result.pos.x + 0.1, result.pos.y, result.pos.z)
                }

                else -> {}
            }
            world.spawnEntity(richochet)
        }
        return 0
    }

}