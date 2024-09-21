package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.enchantment.Enchantment
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
import net.minecraft.registry.RegistryKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralDamageTypeTags
import org.teamvoided.astralarsenal.data.tags.AstralEntityTags.MOUNTS_WITH_DASH
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.DamageModificationStage
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class DashKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_DASH) }),
    AirSpeedKosmogliph {
    // change this to change how much boost they get :3
    val JUMP_FORWARD_BOOST = 1.0

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.DASH_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world
        var dashingEntity: LivingEntity = player

        if (player.vehicle != null) {
            val entity = player.vehicle!!
            if (entity is LivingEntity && entity.type.isIn(MOUNTS_WITH_DASH)) {
                dashingEntity = entity
            } else return
        }

        if (dashingEntity.isClimbing) return

        if (data.uses > 0) {
            val boo = JUMP_FORWARD_BOOST * if (dashingEntity.health <= (dashingEntity.maxHealth * 0.25)) 0.75 else 1.0
            val boost = dashingEntity.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(boo)
            dashingEntity.setVelocity(dashingEntity.velocity.x + boost.x, 0.1, dashingEntity.velocity.z + boost.z)
            dashingEntity.velocityModified = true
            world.playSound(
                null,
                dashingEntity.x,
                dashingEntity.y,
                dashingEntity.z,
                SoundEvents.ENTITY_BREEZE_LAND,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
            )
            if (world is ServerWorld) {
                repeat(20) {
                    world.spawnParticles(
                        ParticleTypes.CLOUD,
                        dashingEntity.x + (world.random.nextDouble() - 0.5) * 1.7,
                        dashingEntity.y + 1 + (world.random.nextDouble() - 0.5) * 1.7,
                        dashingEntity.z + (world.random.nextDouble() - 0.5) * 1.7,
                        0,
                        dashingEntity.velocity.x,
                        dashingEntity.velocity.y,
                        dashingEntity.velocity.z,
                        -0.2,
                    )
                }
            }
            stack.set(AstralItemComponents.DASH_DATA, Data(data.uses - 1, data.cooldown))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super<AirSpeedKosmogliph>.inventoryTick(stack, world, entity, slot, selected)
        if (slot == 1) {
            val data = stack.get(AstralItemComponents.DASH_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var uses = data.uses
            if (uses >= 3) return
            var cooldown = data.cooldown
            if (entity is PlayerEntity) {
                if (entity.hungerManager.foodLevel > 6) {
                    cooldown--
                }
            } else {
                cooldown--
            }

            if (cooldown <= 0) {
                uses++
                val x: Float = (uses * 2.0).toFloat()
                var time = 20
                if (entity is LivingEntity) {
                    val y = entity.statusEffects.filter { it.effectType == StatusEffects.SLOWNESS }
                    if (y.isNotEmpty()) {
                        for (t in y) {
                            time += (t.amplifier * 20)
                        }
                    }
                    val z: Int = (entity.frozenTicks / 20)
                    time += z
                }
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

            stack.set(AstralItemComponents.DASH_DATA, Data(uses, cooldown))
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
        if (stage != DamageModificationStage.PRE_ARMOR) return super<SimpleKosmogliph>.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)

        val data = stack.get(AstralItemComponents.DASH_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = data.cooldown
        if (!entity.world.isClient)
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
        stack.set(AstralItemComponents.DASH_DATA, Data(uses, cooldown))
        return super<SimpleKosmogliph>.modifyDamage(stack, entity, damage, source, equipmentSlot, stage)
    }

    data class Data(
        val uses: Int,
        val cooldown: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0], list[1]) },
                    { data -> listOf(data.uses, data.cooldown) }
                )
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}