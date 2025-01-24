package org.teamvoided.astralarsenal.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.DodgeData
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.util.hasKosmogliph
import kotlin.math.max
import kotlin.math.sqrt

class DodgeKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_DODGE) }),
    AirSpeedKosmogliph {
    val JUMP_FORWARD_BOOST = 5.0
    val SPEED_CAP = 1.0
    val SPEED_MULT = sqrt(2 * SPEED_CAP * SPEED_CAP)

    fun handleJump(
        stack: ItemStack,
        player: PlayerEntity,
        forward: Boolean,
        backward: Boolean,
        left: Boolean,
        right: Boolean
    ) {
        if (stack.hasKosmogliph(AstralKosmogliphs.DODGE)) {
            val data = stack.get(AstralDataComponents.DODGE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            val world = player.world
            if (world is ServerWorld) return
            var LRbias = 0 // 1 is right, -1 is left, 0 is neither
            var BFbias = 0 // 1 for forward, -1 for back, 0 for neither
            if (left xor right) {
                LRbias = if (left) 1 else -1
            }
            if (forward xor backward) {
                BFbias = if (backward) -1 else 1
            }
            if (!(backward || forward || left || right)) {
                BFbias = 1
            }

            val vector = Vec3d(LRbias.toDouble(), 0.0, BFbias.toDouble())
            vector.multiply(player.rotationVector.multiply(1.0, 0.0, 1.0).normalize())

            if (player.vehicle != null || player.isClimbing) return

            if (data.uses > 0 && !player.isFallFlying) {
                println(vector)
                player.velocity = vector
                player.velocityModified = true
                world.playSound(
                    null,
                    player.x,
                    player.y,
                    player.z,
                    SoundEvents.ENTITY_BREEZE_LAND,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
                )
                if (world is ServerWorld) {
                    repeat(20) {
                        world.spawnParticles(
                            ParticleTypes.CLOUD,
                            player.x + (world.random.nextDouble() - 0.5) * 1.7,
                            player.y + 1 + (world.random.nextDouble() - 0.5) * 1.7,
                            player.z + (world.random.nextDouble() - 0.5) * 1.7,
                            0,
                            player.velocity.x,
                            player.velocity.y,
                            player.velocity.z,
                            -0.2,
                        )
                    }
                }
                stack.set(AstralDataComponents.DODGE_DATA, DodgeData(data.uses - 1, data.cooldown))
            }
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super<AirSpeedKosmogliph>.inventoryTick(stack, world, entity, slot, selected)
        if ((entity is PlayerEntity) && entity.inventory.armor.contains(stack) && slot == 1) {
            val data = stack.get(AstralDataComponents.DODGE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var uses = data.uses
            if (uses >= 3) return
            var cooldown = data.cooldown
            if (entity.hungerManager.foodLevel > 6) {
                cooldown--
            }

            if (cooldown <= 0) {
                uses++
                val x: Float = (uses * 2.0).toFloat()
                var time = 20

                val y = entity.statusEffects.filter { it.effectType == StatusEffects.SLOWNESS }
                if (y.isNotEmpty()) {
                    for (t in y) {
                        time += (t.amplifier * 20)
                    }
                }
                val a = entity.statusEffects.filter { it.effectType == StatusEffects.SPEED }
                if (a.isNotEmpty()) {
                    for (t in a) {
                        time = max((time * (1.0 / (t.amplifier + 1.0))).toInt(), 1)
                    }
                }
                val z: Int = (entity.frozenTicks / 20)
                time += z

                cooldown = time
                if (entity is ServerPlayerEntity) {
                    entity.networkHandler.send(
                        SoundPlayS2CPacket(
                            Holder.createDirect(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE),
                            SoundCategory.PLAYERS,
                            entity.x,
                            entity.y,
                            entity.z,
                            1.6F,
                            x,
                            world.getRandom().nextLong()
                        )
                    )
                }
            }

            stack.set(AstralDataComponents.DODGE_DATA, DodgeData(uses, cooldown))
        }
    }

    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot,
        stage: DamageModificationStage
    ): Float {
        if (stage != DamageModificationStage.POST_EFFECT) return super<SimpleKosmogliph>.modifyDamage(
            stack,
            entity,
            damage,
            source,
            equipmentSlot,
            stage
        )

        val data = stack.get(AstralDataComponents.DODGE_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = data.cooldown
        if (damage >= 5 && !source.isTypeIn(AstralDamageTypeTags.KEEPS_MOVEMENT) && entity.lastDamageTaken < damage) {
            if (uses >= 3) {
                uses += -1
                cooldown += 20
            } else if (cooldown >= 100 && uses != 0) {
                uses += -1
            } else if (cooldown <= 100) {
                cooldown += 15
            }
        }
        stack.set(AstralDataComponents.DODGE_DATA, DodgeData(uses, cooldown))
        return super<SimpleKosmogliph>.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)
    }

}