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
import org.joml.Math.lerp
import org.teamvoided.astralarsenal.components.CapacitanceData
import org.teamvoided.astralarsenal.components.CapacitanceDataV2
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamRenderEntity
import org.teamvoided.astralarsenal.init.AstralDamageTypes
import org.teamvoided.astralarsenal.init.AstralDamageTypes.customDamage
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CapacitanceKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_CAPACITANCE) }) {
    val CHARGE_DRAIN_PER_SECOND = 0.05f
    val MAX_PLAYER_DAMAGE = 15f
    val DISCHARGE_PERCENT_PER_HIT = 1.0f
    val DAMAGE_TO_CHARGE = 0.75
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
        val data = stack.get(AstralDataComponents.CAPACITANCE_DATA_V1)
            ?: throw IllegalStateException("how the fuck?")
        val dataTwo = stack.get(AstralDataComponents.CAPACITANCE_DATA_V2)
            ?: throw IllegalStateException("wuh?")
        var dmg = data.damage
        var dischargeTime = dataTwo.dischargeTime
        var countdownTime = dataTwo.countdownTime
        var outputDamage = damage
        if (source.isTypeIn(AstralDamageTypeTags.IS_PLASMA) || source.attacker is GuardianEntity || source.attacker is ElderGuardianEntity) {
            outputDamage = (outputDamage * 0.2).toFloat()
            countdownTime = TICKS_BEFORE_DISCHARGE
            dmg += (damage * DAMAGE_TO_CHARGE).toFloat()
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
                dmg += (damage * DAMAGE_TO_CHARGE).toFloat()
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
                    sillyLightningTime(entity.pos, attacker.pos, world)
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
        stack.set(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceData(dmg))
        stack.set(AstralDataComponents.CAPACITANCE_DATA_V2, CapacitanceDataV2(dischargeTime, countdownTime))
        return outputDamage
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (entity is LivingEntity && entity.getEquippedStack(EquipmentSlot.CHEST) == stack) {
            val data = stack.get(AstralDataComponents.CAPACITANCE_DATA_V1)
                ?: throw IllegalStateException("how the fuck?")
            val data2 = stack.get(AstralDataComponents.CAPACITANCE_DATA_V2)
                ?: throw IllegalStateException("why?")
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
                        max(1.0, (data.damage * 0.2)).roundToInt(),
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
                if (dischargeTime <= 0) {
                    shockNearbyEntities(entity, entity, damage)
                    damage = 0.0f
                }
            }
            stack.set(AstralDataComponents.CAPACITANCE_DATA_V1, CapacitanceData(damage))
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
            ).filter { it is LivingEntity && it != cause && it != base }
        )
        val targets = entities.size
        val damagePerEntity = damage / targets
        if (entities.isNotEmpty()) {
            var count = 0
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
                    sillyLightningTime(base.pos, entiity.pos, ((base.world as ServerWorld)))
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

    fun sillyLightningTime(pos1: Vec3d, pos2: Vec3d, world: ServerWorld) {
        val bends = world.random.rangeInclusive(3, 5)
        val bendPos = mutableListOf<Vec3d>()
        bendPos.add(pos1)
        for (i in 0..<bends) {
            val distance = pos1.distanceTo(pos2)
            val maxlerp: Double = 1.0 / bends
            val xrand = (pos1.x - pos2.x) / (bends / 2)
            val yrand = (pos1.y - pos2.y) / (bends / 2)
            val zrand = (pos1.z - pos2.z) / (bends / 2)
            val xmin = ((pos1.x - pos2.x) / (bends)) * i
            val ymin = ((pos1.y - pos2.y) / (bends)) * i
            val zmin = ((pos1.z - pos2.z) / (bends)) * i
            bendPos.add(
                Vec3d(
                    lerp(pos1.x, pos2.x, maxlerp) + xmin + world.random.nextDouble()
                        .minus(0.5).times(xrand),
                    lerp(pos1.y, pos2.y, maxlerp) + ymin + world.random.nextDouble()
                        .minus(0.5).times(yrand),
                    lerp(pos1.z, pos2.z, maxlerp) + zmin + world.random.nextDouble()
                        .minus(0.5).times(zrand)
                )
            )
        }
        bendPos.add(pos2)
        var count = 0
        for (i in 0..<(bendPos.size - 1)) {
            if (count > bendPos.size) {
                break
            }
            count++
            val a = bendPos[i]
            val b = bendPos[i + 1]
            val distance = a.distanceTo(b)
            val interval = (distance * 10)
            val beamRenderer = BeamRenderEntity(world, a.x, a.y + 1, a.z)
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterColour, 0x00ababab.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.InterColour, 0x00ababab.toInt())
            beamRenderer.dataTracker.set(BeamRenderEntity.LiveTime, 10)
            beamRenderer.dataTracker.set(BeamRenderEntity.ShrinkTime, 5)
            beamRenderer.dataTracker.set(BeamRenderEntity.TargetPos, Vec3d(b.x, b.y + 1, b.z).toVector3f())
            beamRenderer.dataTracker.set(BeamRenderEntity.OuterThickness, 0.05f)
            beamRenderer.dataTracker.set(BeamRenderEntity.MaxOuterThickness, 0.05f)
            beamRenderer.dataTracker.set(BeamRenderEntity.InnerCubes, 1)
            beamRenderer.setPosition(a.x, a.y + 1, a.z)
            world.spawnEntity(beamRenderer)
        }
    }
}