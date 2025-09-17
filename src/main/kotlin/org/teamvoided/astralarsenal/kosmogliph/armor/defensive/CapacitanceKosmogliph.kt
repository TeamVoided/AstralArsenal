package org.teamvoided.astralarsenal.kosmogliph.armor.defensive

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.CapacitanceDataV1
import org.teamvoided.astralarsenal.components.CapacitanceDataV2
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.Arrows.ChargedArrow
import org.teamvoided.astralarsenal.entity.Arrows.ChargedSpectralArrow
import org.teamvoided.astralarsenal.entity.Projectiles.CannonballEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralEffects.BREACHED
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData
import org.teamvoided.astralarsenal.util.sillyLightningTime
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CapacitanceKosmogliph(id: Identifier) :
    KosmogliphWithData(id, AstralDataComponents.CAPACITANCE_DATA_V1, AstralItemTags.SUPPORTS_CAPACITANCE) {
    val MAX_PLAYER_DAMAGE = 10f
    val DISCHARGE_PERCENT_PER_HIT = 1.0f
    val DAMAGE_TO_CHARGE = 1.0
    val TICKS_TO_DISCHARGE = 100
    val TICKS_BEFORE_DISCHARGE = 100

    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_ARMOR) return super.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )
        val effects = entity.statusEffects.filter { breached.contains(it.effectType) }
        var multiplyer = 0.2
        var dtc = 1.0
        for (effect in effects) {
            multiplyer = min(0.2 + (0.2 * (effect.amplifier + 1)), 1.0)
            dtc = max(0.0, 1 - (0.25 * (effect.amplifier + 1)))
        }
        val data = stack.getOrDefault(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceDataV1.DEFAULT)
        val dataTwo = stack.getOrDefault(AstralDataComponents.CAPACITANCE_DATA_V2, CapacitanceDataV2.DEFAULT)
        var dmg = data.damage
        var dischargeTime = dataTwo.dischargeTime
        var countdownTime = dataTwo.countdownTime
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA) || source.attacker is GuardianEntity || source.attacker is ElderGuardianEntity) {
            outputDamage = (outputDamage * multiplyer).toFloat()
            countdownTime = TICKS_BEFORE_DISCHARGE
            dmg += (damage * DAMAGE_TO_CHARGE * dtc).toFloat()
            entity.world.playSound(
                null,
                entity.x,
                entity.y,
                entity.z,
                SoundEvents.BLOCK_COPPER_BULB_BREAK,
                SoundCategory.PLAYERS,
                1.0F,
                1.4f
            )
        } else {
            if (countdownTime > 0) {
                countdownTime = TICKS_BEFORE_DISCHARGE
                dmg += (damage * DAMAGE_TO_CHARGE * dtc).toFloat()
                entity.world.playSound(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    SoundEvents.BLOCK_COPPER_BULB_BREAK,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.4f
                )
            } else if (dischargeTime > 0) {
                if (source.attacker is LivingEntity && entity.world is ServerWorld && source.attacker != entity) {
                    val attacker = source.attacker as LivingEntity
                    val world = entity.world as ServerWorld
                    var damageToDeal = if (attacker is PlayerEntity) min(
                        MAX_PLAYER_DAMAGE,
                        dmg * DISCHARGE_PERCENT_PER_HIT
                    ) else dmg * DISCHARGE_PERCENT_PER_HIT
                    if (damageToDeal > attacker.health) {
                        damageToDeal = attacker.health
                    }
                    attacker.customDamage(AstralDamageTypes.RICHOCHET, damageToDeal, entity, entity)
                    sillyLightningTime(
                        Vec3d(entity.pos.x, entity.pos.y + (entity.height / 2f), entity.pos.z),
                        Vec3d(attacker.pos.x, attacker.pos.y + (attacker.height / 2f), attacker.pos.z),
                        world,
                        3,
                        5,
                        6,
                        0.1f,
                        5.0
                    )
                    if (dmg != damageToDeal) {
                        shockNearbyEntities(entity, attacker, dmg - damageToDeal)
                    } else {
                        if (entity.world is ServerWorld) {
                            val sworld = entity.world as ServerWorld
                            sworld.spawnParticles(
                                ParticleTypes.END_ROD,
                                entity.x,
                                entity.eyeY,
                                entity.z,
                                20,
                                0.0,
                                0.0,
                                0.0,
                                0.3
                            )
                        }
                    }
                    entity.world.playSound(
                        null,
                        entity.x,
                        entity.y,
                        entity.z,
                        SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.4f
                    )
                    dmg = 0.0f
                    dischargeTime = 0
                }
            }
        }
        stack.set(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceDataV1(dmg))
        stack.set(AstralDataComponents.CAPACITANCE_DATA_V2, CapacitanceDataV2(dischargeTime, countdownTime))
        return outputDamage
    }

    val breached = listOf(
        BREACHED
    )


    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity is LivingEntity && entity.getEquippedStack(EquipmentSlot.CHEST) == stack) {
            val data = stack.getOrDefault(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceDataV1.DEFAULT)
            val data2 = stack.getOrDefault(AstralDataComponents.CAPACITANCE_DATA_V2, CapacitanceDataV2.DEFAULT)
            var damage = data.damage
            var dischargeTime = data2.dischargeTime
            var countdownTime = data2.countdownTime
            if (data.damage >= 0.5) {
                val height = entity.height
                val width = entity.width
                if (world is ServerWorld) {
                    world.spawnParticles(
                        ParticleTypes.ELECTRIC_SPARK,
                        entity.x,
                        entity.y + (height / 2),
                        entity.z,
                        min(max(1.0, (data.damage * 0.2)), 4.0).roundToInt(),
                        width / 2.5,
                        height / 2.5,
                        width / 2.5,
                        0.0
                    )
                }
            }
            if (countdownTime > 0) {
                countdownTime--
                if (countdownTime <= 0) {
                    dischargeTime = TICKS_TO_DISCHARGE
                    entity.world.playSound(
                        null,
                        entity.x,
                        entity.y,
                        entity.z,
                        SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.6f
                    )
                }
            } else if (dischargeTime > 0) {
                dischargeTime--
                if (world is ServerWorld && world.time % 2 == 0L) {
                    repeat(2){
                    sillyLightningTime(
                        Vec3d(entity.pos.x, entity.pos.y + (entity.height / 2f), entity.pos.z),
                        Vec3d(
                            entity.x + (world.random.nextDouble().minus(0.5) * 5),
                            entity.y + (world.random.nextDouble().minus(0.5) * 5) + (entity.height / 2f),
                            entity.z + (world.random.nextDouble().minus(0.5) * 5)
                        ), world, 1, 9, 2, 0.05f, 0.75
                    )}
                    sparkNearbyEntities(entity, entity)
                }
                if (dischargeTime <= 0) {
                    shockNearbyEntities(entity, entity, damage)
                    damage = 0.0f
                }
            }
            stack.set(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceDataV1(damage))
            stack.set(AstralDataComponents.CAPACITANCE_DATA_V2, CapacitanceDataV2(dischargeTime, countdownTime))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
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
            ).filter { it is LivingEntity && it != cause && it != base && base.distanceTo(it) <= 10 || (it is ChargedArrow || it is ChargedSpectralArrow)}
        )
        val targets = entities.size
        val damagePerEntity = damage / targets
        if (entities.isNotEmpty()) {
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
                if (entiity is ChargedArrow) {
                    entiity.isCharged = true
                    entiity.chargeDamage = tempDamageValue
                    entiity.ticksBeforeDischarge = 5
                    cause.let { entiity.chargeChain.add(it) }
                    sillyLightningTime(cause.eyePos, entiity.eyePos, cause.world as ServerWorld, 3, 5, 2, 0.1f, 0.5)
                } else if (entiity is ChargedSpectralArrow) {
                    entiity.isCharged = true
                    entiity.chargeDamage = tempDamageValue
                    entiity.ticksBeforeDischarge = 5
                    cause.let { entiity.chargeChain.add(it) }
                    sillyLightningTime(cause.eyePos, entiity.eyePos, cause.world as ServerWorld, 3, 5, 2, 0.1f, 0.5)
                }
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
                cause.x,
                cause.y,
                cause.z,
                SoundEvents.ITEM_TRIDENT_THUNDER.value(),
                SoundCategory.PLAYERS,
                1.0F,
                1.4f
            )
        } else {
            cause.world.playSound(
                null,
                cause.x,
                cause.y,
                cause.z,
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
        if (cause.world is ServerWorld) {
            val sworld = cause.world as ServerWorld
            sworld.spawnParticles(
                ParticleTypes.END_ROD,
                cause.x,
                cause.eyeY,
                cause.z,
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
            ).filter { (it is LivingEntity || it is CannonballEntity) && it != cause && it != base && base.distanceTo(it) <= 10 }
        )
        if (entities.isNotEmpty()) {
            for (entiity in entities) {
                if (base.world is ServerWorld) {
                    sillyLightningTime(
                        Vec3d(base.pos.x, base.pos.y + (base.height / 2f), base.pos.z),
                        Vec3d(entiity.pos.x, entiity.pos.y + (entiity.height / 2f), entiity.pos.z),
                        ((base.world as ServerWorld)),
                        2,
                        5,
                        2,
                        0.03f,
                        0.5
                    )
                    if (entiity is CannonballEntity){
                        entiity.setCharged(true)
                    }
                }
            }
        }
        if (cause.world is ServerWorld && cause.world.time % 3.0 == 0.0) {
            val sworld = cause.world as ServerWorld
            sworld.spawnParticles(
                ParticleTypes.END_ROD,
                cause.x,
                cause.eyeY,
                cause.z,
                1,
                0.0,
                0.0,
                0.0,
                0.3
            )
        }
    }
}