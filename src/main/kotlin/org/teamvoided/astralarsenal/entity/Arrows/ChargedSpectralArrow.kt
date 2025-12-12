package org.teamvoided.astralarsenal.entity.Arrows

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.SpectralArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralEffects
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.util.sillyLightningTime
import kotlin.math.max

class ChargedSpectralArrow : SpectralArrowEntity {

    constructor(entityType: EntityType<out ChargedSpectralArrow>, world: World) :
            super(entityType, world)

    constructor(world: World, owner: LivingEntity, arrow: ItemStack, weapon: ItemStack) : super(
        world, owner, arrow, weapon
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        world, x, y, z, Items.ARROW.defaultStack, AstralItems.NAILCANNON.defaultStack
    )

    var chargeChain = mutableListOf<Entity>()
    var isCharged = false
    var chargeDamage: Float = 0f
    var ticksBeforeDischarge = 1
    val MAX_PLAYER_DAMAGE = 1f
    var shocksBeforeDiscard = 3

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val hit = entityHitResult.entity
        if (hit is LivingEntity) {
            var effectLevel = 0
            val currentEffect = hit.statusEffects.find { it.effectType == AstralEffects.CONDUCTIVE }
            currentEffect?.let { effectLevel = it.amplifier + 1 }
            if (hit.world is ServerWorld) {
                hit.removeStatusEffect(AstralEffects.CONDUCTIVE)
                hit.addStatusEffect(
                    StatusEffectInstance(
                        AstralEffects.CONDUCTIVE,
                        400, effectLevel,
                        false, false, true
                    )
                )
            }
        }
        super.onEntityHit(entityHitResult)
    }

    override fun tick() {
        if (this.isCharged) {
            if (this.ticksBeforeDischarge > 0) {
                ticksBeforeDischarge--
            } else if (this.world is ServerWorld && checkNearbyArrows(this.world as ServerWorld)) {
                var bool = false
                if (this.owner != null) {
                    bool = shockNearbyEntities(this.owner!!, this, chargeDamage)
                } else {
                    bool = shockNearbyEntities(this, this, chargeDamage)
                }
                val preservedDamage = if (bool) chargeDamage * 0.8f else chargeDamage
                val nearestArrow = getNearestArrow(this.world as ServerWorld)
                chargeChain.add(this)
                if (nearestArrow is ChargedArrow) {
                    nearestArrow.isCharged = true
                    nearestArrow.chargeDamage = preservedDamage * 1f
                    nearestArrow.chargeChain = chargeChain
                    nearestArrow.ticksBeforeDischarge = 5
                    shocksBeforeDiscard--
                    if (!this.inGround){
                        shocksBeforeDiscard = 0
                    }
                    sillyLightningTime(this.eyePos, nearestArrow.eyePos, this.world as ServerWorld, 3, 5, 2, 0.01f, 0.5)
                    isCharged = false
                    chargeChain = mutableListOf<Entity>()
                    if (shocksBeforeDiscard <= 0) this.discard()
                } else if (nearestArrow is ChargedSpectralArrow) {
                    nearestArrow.isCharged = true
                    nearestArrow.chargeDamage = preservedDamage * 1f
                    nearestArrow.chargeChain = chargeChain
                    nearestArrow.ticksBeforeDischarge = 5
                    shocksBeforeDiscard--
                    if (!this.inGround){
                        shocksBeforeDiscard = 0
                    }
                    sillyLightningTime(this.eyePos, nearestArrow.eyePos, this.world as ServerWorld, 3, 5, 2, 0.01f, 0.5)
                    isCharged = false
                    chargeChain = mutableListOf<Entity>()
                    if (shocksBeforeDiscard <= 0) this.discard()
                }
            } else {
                if (this.owner != null) {
                    shockNearbyEntities(this.owner!!, this, chargeDamage)
                } else {
                    shockNearbyEntities(this, this, chargeDamage)
                }
                shocksBeforeDiscard--
                isCharged = false
                chargeChain = mutableListOf<Entity>()
                if (shocksBeforeDiscard <= 0) this.discard()
            }
        }
        super.tick()
    }

    fun checkNearbyArrows(world: ServerWorld): Boolean {
        val nearbyArrows = mutableListOf<Entity>()
        nearbyArrows.addAll(
            world.getOtherEntities(
                this, Box(
                    this.x + 10,
                    this.y + 10,
                    this.z + 10,
                    this.x - 10,
                    this.y - 10,
                    this.z - 10
                )
            ).filter { (it is ChargedArrow || it is ChargedSpectralArrow) && !chargeChain.contains(it) }
        )
        return nearbyArrows.isNotEmpty()
    }

    fun getNearestArrow(world: ServerWorld): PersistentProjectileEntity? {
        val nearbyArrows = mutableListOf<Entity>()
        nearbyArrows.addAll(
            world.getOtherEntities(
                this, Box(
                    this.x + 10,
                    this.y + 10,
                    this.z + 10,
                    this.x - 10,
                    this.y - 10,
                    this.z - 10
                )
            ).filter { (it is ChargedArrow || it is ChargedSpectralArrow) && !chargeChain.contains(it) }
        )
        var nearestArrow: PersistentProjectileEntity? = null
        for (arrow in nearbyArrows) {
            if (nearestArrow != null) {
                val distanceToArrow = nearestArrow.distanceTo(this)
                val distanceToNewArrow = arrow.distanceTo(this)
                if (distanceToNewArrow < distanceToArrow) {
                    nearestArrow = arrow as PersistentProjectileEntity?
                }
            } else {
                nearestArrow = arrow as PersistentProjectileEntity?
            }
        }
        return nearestArrow
    }

    fun shockNearbyEntities(cause: Entity, base: Entity, damage: Float): Boolean {
        val entities = mutableListOf<Entity>()
        var bool = false
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
                it is LivingEntity && it != cause && it != base && base.distanceTo(it) <= 10 && !chargeChain.contains(it)
            }
        )
        val targets = entities.size
        val damagePerEntity = damage / (max(targets / 3f, 1f))
        if (entities.isNotEmpty()) {
            bool = true
            for (entiity in entities) {
                var tempDamageValue = damagePerEntity
                if (entiity is PlayerEntity && tempDamageValue >= MAX_PLAYER_DAMAGE) {
                    tempDamageValue = MAX_PLAYER_DAMAGE
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
                        3,
                        0.05f,
                        0.5
                    )
                }
            }
            base.world.playSound(
                null,
                base.x,
                base.y,
                base.z,
                SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                SoundCategory.PLAYERS,
                1.0F,
                1.4f
            )
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
        return bool
    }
}