package org.teamvoided.astralarsenal.entity.entitiesThatAreJustHereCauseOtherShitDontWork

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
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.Arrows.ChargedArrow
import org.teamvoided.astralarsenal.entity.Arrows.ChargedSpectralArrow
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEntities
import org.teamvoided.astralarsenal.util.sillyLightningTime
import org.teamvoided.astralarsenal.world.explosion.WeakExplosionBehavior

class ConductiveEntity : Entity {
    var owner: Entity? = null
    var origin: LivingEntity? = null
    var maxTargets = 0.0
    var dmg = 0.0f
    var cooldown = 0

    constructor(entityType: EntityType<out ConductiveEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(AstralEntities.CONDUCTIVITY_ENGINE as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(AstralEntities.CONDUCTIVITY_ENGINE as EntityType<out Entity?>, world)

    override fun tick() {

        if (this.cooldown > 0) this.cooldown--
        else {
            val entities = mutableListOf<Entity>()
            entities.addAll(
                this.world.getOtherEntities(
                    this, Box(
                        this.x + 10,
                        this.y + 10,
                        this.z + 10,
                        this.x - 10,
                        this.y - 10,
                        this.z - 10
                    )
                ).filter {
                    (it is LivingEntity || it is CannonballEntity || it is ChargedArrow || it is ChargedSpectralArrow) && it != owner && it != origin && ((origin != null && origin!!.distanceTo(
                        it
                    ) <= 10) || origin == null)
                }
            )
            if (entities.isNotEmpty()) {
                repeat((entities.size - maxTargets).toInt()) {
                    entities.removeAt(this.world.random.rangeInclusive(0, entities.size - 1))
                }
                var count = 0
                var tempDamage = dmg
                for (entiity in entities) {
                    if (count >= maxTargets) {
                        break
                    }
                    if (entiity is PlayerEntity && tempDamage >= 2.5) {
                        tempDamage = 2.5f
                    }
                    if (entiity is CannonballEntity) {
                        entiity.setCharged(true)
                    }
                    if (entiity is ChargedArrow) {
                        entiity.isCharged = true
                        entiity.chargeDamage = tempDamage
                        entiity.ticksBeforeDischarge = 5
                        owner?.let { entiity.chargeChain.add(it) }
                        sillyLightningTime(this.eyePos, entiity.eyePos, this.world as ServerWorld, 3, 5, 2, 0.1f, 0.5)
                    } else if (entiity is ChargedSpectralArrow) {
                        entiity.isCharged = true
                        entiity.chargeDamage = tempDamage
                        entiity.ticksBeforeDischarge = 5
                        owner?.let { entiity.chargeChain.add(it) }
                        sillyLightningTime(this.eyePos, entiity.eyePos, this.world as ServerWorld, 3, 5, 2, 0.1f, 0.5)
                    }
                    entiity.damage(
                        DamageSource(
                            AstralDamageTypes.getHolder(
                                this.world.registryManager,
                                if (entiity is PlayerEntity) AstralDamageTypes.NON_RAILED else AstralDamageTypes.RICHOCHET
                            ),
                            owner,
                            owner,
                        ), tempDamage
                    )
                    if (this.world is ServerWorld) {
                        sillyLightningTime(
                            this.eyePos,
                            Vec3d(entiity.x, entiity.y + (entiity.height / 2), entiity.z),
                            ((this.world as ServerWorld)),
                            3, 5, 5, 0.05f, 1.0
                        )
                    }
                    if (this.world is ServerWorld) {
                        val sworld = this.world as ServerWorld
                        sworld.spawnParticles(
                            ParticleTypes.END_ROD,
                            this.x,
                            this.eyeY,
                            this.z,
                            3,
                            0.0,
                            0.0,
                            0.0,
                            0.1
                        )
                        sworld.spawnParticles(
                            ParticleTypes.END_ROD,
                            entiity.x,
                            entiity.eyeY,
                            entiity.z,
                            3,
                            0.0,
                            0.0,
                            0.0,
                            0.1
                        )
                    }
                    count++
                }
                this.world.playSound(
                    null,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.6f
                )
                this.discard()
            } else {
                if (this.world is ServerWorld) {
                    val sworld = this.world as ServerWorld
                    sworld.spawnParticles(
                        ParticleTypes.END_ROD,
                        this.x,
                        this.eyeY,
                        this.z,
                        5,
                        0.0,
                        0.0,
                        0.0,
                        0.3
                    )
                }
                this.discard()
            }
        }
        super.tick()
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}