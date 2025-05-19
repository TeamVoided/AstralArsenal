package org.teamvoided.astralarsenal.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ProjectileDeflector
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.util.sillyLightningTime
import org.teamvoided.astralarsenal.world.explosion.KnockbackExplosionBehavior

class CannonballEntity : ThrownItemEntity {

    constructor(entityType: EntityType<out CannonballEntity>, world: World?) :
            super(entityType, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.CANNONBALL_ENTITY, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.CANNONBALL_ENTITY, x, y, z, world)

    override fun getDefaultItem(): Item {
        return AstralItems.CANNONBALL
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entity = entityHitResult.entity
        if (entity is CannonballEntity) return
        if (entity !is PlayerEntity) {
            entity.customDamage(AstralDamageTypes.CANNONBALL, getDmg().toFloat(), this, owner)
            if (entity is EndermanEntity) return
        } else {
            val type = if (entity == owner) AstralDamageTypes.BALLNT else AstralDamageTypes.CANNONBALL
            entity.customDamage(type, getDmg().toFloat(), this, owner)
        }
        if (this.getDmg() in 20..39) {
            this.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND)
        } else if (this.getDmg() >= 40) {
            this.playSound((SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY))
            entity.setOnFireFor(100)
        } else {
            this.playSound(SoundEvents.ITEM_MACE_SMASH_AIR)
        }
        var i: Int = this.getDmg() + 5
        if (i > 40) {
            i = 40
        }
        this.setDmg(i)
        if ((this.getDmg() < 20)) {
            this.setVelocity(this.velocity.multiply(-0.05, 0.0, -0.05))
            this.addVelocity(0.0, 0.2, 0.0)
        }
        if (getCharged()) {
            entityTime.forEachIndexed { index, number ->
                entityTime.set(index, number + 100)
            }
        }
    }

    override fun deflect(
        projectileDeflector: ProjectileDeflector,
        entity2: Entity?,
        entity: Entity?,
        bl: Boolean
    ): Boolean {
        if (!world.isClient) {
            deflecting.deflect(this, entity2, this.random)
            if (entity != null && getCharged()) {
                entity.customDamage(
                    if (entity is PlayerEntity) AstralDamageTypes.NON_RAILED else AstralDamageTypes.RICHOCHET,
                    2f,
                    this,
                    this.owner
                )
                if (this.world is ServerWorld) {
                    sillyLightningTime(
                        Vec3d(this.pos.x, this.pos.y + (this.height / 2f), this.pos.z),
                        Vec3d(entity.pos.x, entity.pos.y + (entity.height / 2f), entity.pos.z),
                        this.world as ServerWorld,
                        3,
                        5,
                        6,
                        0.1f,
                        0.75
                    )
                    this.world.playSound(
                        null,
                        this.x,
                        this.y,
                        this.z,
                        SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.4f
                    )
                }
            }
            this.owner = entity
            this.onDeflected(entity2, bl)
        }

        return true
    }

    companion object {
        val deflecting: ProjectileDeflector =
            ProjectileDeflector { projectileEntity: ProjectileEntity, entity: Entity?, random: RandomGenerator? ->
                if (entity != null) {
                    val vec3d = entity.rotationVector.normalize().multiply(2.5)
                    projectileEntity.velocity = vec3d
                    projectileEntity.velocityDirty = true
                    projectileEntity.playSound(SoundEvents.ITEM_MACE_SMASH_AIR)
                }
            }
        private val DMG: TrackedData<Int>? =
            DataTracker.registerData(CannonballEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val CHARGED: TrackedData<Boolean>? =
            DataTracker.registerData(CannonballEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(DMG, 10)
        builder.add(CHARGED, false)
    }

    val entityList = mutableListOf<LivingEntity>()
    val entityTime = mutableListOf<Int>()

    override fun tick() {
        if (getCharged()) {
            if (world is ServerWorld) {
                sillyLightningTime(
                    Vec3d(this.pos.x, this.pos.y + (this.height / 2f), this.pos.z),
                    Vec3d(
                        this.x + (world.random.nextDouble().minus(0.5) * 3),
                        this.y + (world.random.nextDouble().minus(0.5) * 3) + (this.height / 2f),
                        this.z + (world.random.nextDouble().minus(0.5) * 3)
                    ), world as ServerWorld, 1, 9, 2, 0.05f, 0.75
                )
            }
            sparkNearbyEntities(if (this.owner != null) this.owner!! else this, this)
            val entities = mutableListOf<Entity>()
            entities.addAll(
                this.world.getOtherEntities(
                    this.owner, Box(
                        this.x + 10,
                        this.y + 10,
                        this.z + 10,
                        this.x - 10,
                        this.y - 10,
                        this.z - 10
                    )
                ).filter { it is LivingEntity && it != this.owner && it != this && this.distanceTo(it) <= 10 }
            )
            if (entities.isNotEmpty()) {
                for (entity in entities) {
                    var number: Int? = null
                    if (entityList.contains(entity)) {
                        number = entityList.indexOf(entity)
                        var increment = entityTime.get(number) + 1
                        if (increment >= 15) {
                            entity.customDamage(
                                if (entity is PlayerEntity) AstralDamageTypes.NON_RAILED else AstralDamageTypes.RICHOCHET,
                                2f,
                                this,
                                this.owner
                            )
                            if (this.world is ServerWorld) {
                                sillyLightningTime(
                                    Vec3d(this.pos.x, this.pos.y + (this.height / 2f), this.pos.z),
                                    Vec3d(entity.pos.x, entity.pos.y + (entity.height / 2f), entity.pos.z),
                                    this.world as ServerWorld,
                                    3,
                                    5,
                                    6,
                                    0.1f,
                                    0.75
                                )
                                this.world.playSound(
                                    null,
                                    this.x,
                                    this.y,
                                    this.z,
                                    SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                                    SoundCategory.PLAYERS,
                                    1.0F,
                                    1.4f
                                )
                            }
                            increment = 1
                        }
                        entityTime.set(number, increment)
                    } else {
                        entityList.add(entity as LivingEntity)
                        entityTime.add(1)
                    }
                }
            }
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if (this.getDmg() < 20) {
            this.setDmg(20)
        }
        if (this.owner != null) {
            world.createExplosion(
                null,
                damageSources.explosion(null, this.owner),
                KnockbackExplosionBehavior(this.owner!!),
                this.x,
                this.y,
                this.z,
                2.0f,
                false,
                World.ExplosionSourceType.TNT
            )
        }
        if (this.owner != null && getCharged()) {
            shockNearbyEntities(this.owner!!, this, 25f)
        }
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    fun setDmg(dmg: Int) {
        dataTracker.set(DMG, dmg)
    }

    fun getDmg(): Int {
        return dataTracker.get(DMG) as Int
    }

    fun setCharged(charged: Boolean) {
        dataTracker.set(CHARGED, charged)
    }

    fun getCharged(): Boolean {
        return dataTracker.get(CHARGED) as Boolean
    }

    fun shockNearbyEntities(cause: Entity, base: Entity, damage: Float) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            base.world.getOtherEntities(
                cause, Box(
                    base.x + 10,
                    base.y + 10,
                    base.z + 10,
                    base.x - 10,
                    base.y - 10,
                    base.z - 10
                )
            ).filter { it is LivingEntity && it != cause && it != base && base.distanceTo(it) <= 10 }
        )
        val targets = entities.size
        val damagePerEntity = damage / targets
        if (entities.isNotEmpty()) {
            var count = 0
            for (entiity in entities) {
                var tempDamageValue = damagePerEntity
                if (entiity is PlayerEntity && tempDamageValue >= 5f) {
                    tempDamageValue = 5f
                }
                entiity.damage(
                    DamageSource(
                        AstralDamageTypes.getHolder(
                            cause.world.registryManager,
                            AstralDamageTypes.RICHOCHET
                        ),
                        cause,
                        cause,
                    ), tempDamageValue
                )
                if (base.world is ServerWorld) {
                    sillyLightningTime(
                        Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                        Vec3d(entiity.pos.x, entiity.pos.y + (entiity.height / 2f), entiity.pos.z),
                        ((base.world as ServerWorld)),
                        3,
                        5,
                        6,
                        0.2f,
                        0.5
                    )
                }
            }
            cause.world.playSound(
                null,
                base.x,
                base.y,
                base.z,
                SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                SoundCategory.PLAYERS,
                1.0F,
                1.4f
            )
            if (base.world is ServerWorld) {
                repeat(10) {
                    sillyLightningTime(
                        Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                        Vec3d(
                            base.x + (world.random.nextDouble().minus(0.5) * 9),
                            base.y + (world.random.nextDouble().minus(0.5) * 9) + (base.height / 2f),
                            base.z + (world.random.nextDouble().minus(0.5) * 9)
                        ), world as ServerWorld, 4, 5, 10, 0.05f, 1.0
                    )
                }
            }
        } else {
            cause.world.playSound(
                null,
                base.x,
                base.y,
                base.z,
                SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(),
                SoundCategory.PLAYERS,
                1.0F,
                1.4f
            )
            if (base.world is ServerWorld) {
                val world = base.world as ServerWorld
                repeat(10) {
                    sillyLightningTime(
                        Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                        Vec3d(
                            base.x + (world.random.nextDouble().minus(0.5) * 9),
                            base.y + (world.random.nextDouble().minus(0.5) * 9) + (base.height / 2f),
                            base.z + (world.random.nextDouble().minus(0.5) * 9)
                        ), world, 4, 5, 10, 0.05f, 1.0
                    )
                }
            }
        }
        if (base.world is ServerWorld) {
            val sworld = base.world as ServerWorld
            sworld.spawnParticles(
                ParticleTypes.END_ROD,
                base.x,
                base.eyeY,
                base.z,
                20,
                0.0,
                0.0,
                0.0,
                0.3
            )
        }
    }

    fun sparkNearbyEntities(cause: Entity, base: Entity) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            base.world.getOtherEntities(
                cause, Box(
                    base.x + 10,
                    base.y + 10,
                    base.z + 10,
                    base.x - 10,
                    base.y - 10,
                    base.z - 10
                )
            ).filter {
                (it is LivingEntity || it is CannonballEntity) && it != cause && it != base && base.distanceTo(
                    it
                ) <= 10
            }
        )
        if (entities.isNotEmpty()) {
            for (entiity in entities) {
                if (entiity is CannonballEntity) {
                    entiity.setCharged(true)
                }
                if (base.world is ServerWorld) {
                    sillyLightningTime(
                        Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                        Vec3d(entiity.pos.x, entiity.pos.y + (entiity.height / 2f), entiity.pos.z),
                        ((base.world as ServerWorld)),
                        2,
                        5,
                        1,
                        0.03f,
                        0.5
                    )
                }
            }
        }
        if (base.world is ServerWorld && base.world.time % 3.0 == 0.0) {
            val sworld = base.world as ServerWorld
            sworld.spawnParticles(
                ParticleTypes.END_ROD,
                base.x,
                base.eyeY,
                base.z,
                1,
                0.0,
                0.0,
                0.0,
                0.3
            )
        }
    }
}