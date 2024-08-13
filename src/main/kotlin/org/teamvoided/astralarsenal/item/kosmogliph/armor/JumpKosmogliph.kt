package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
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
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class JumpKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_JUMP) }),
    AirSpeedKosmogliph {
    // Change this to change how much boost is given when double-jumping.
    val JUMP_FORWARD_BOOST = 0.3

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.JUMP_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world

        if (player.vehicle != null) return

        if (data.uses > 0 && !player.isOnGround) {
            var boost = player.rotationVector.multiply(0.0, 0.0, 0.0)
            if (player.velocity.x > 0.0 || player.velocity.z > 0.0) {
                boost = player.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(JUMP_FORWARD_BOOST)
            }
            player.setVelocity(player.velocity.x + boost.x, 0.5, player.velocity.z + boost.z)
            player.velocityModified = true
            player.hungerManager.add(0, -0.1f)
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ENTITY_BREEZE_JUMP,
                SoundCategory.PLAYERS,
                0.3F,
                1.0F
            )
            if (!world.isClient) {
                val serverWorld = world as ServerWorld
                repeat(20) {
                    serverWorld.spawnParticles(
                        ParticleTypes.SPIT,
                        player.x,
                        player.y - 0.1,
                        player.z,
                        1,
                        (world.random.nextDouble() - 0.5) * 1.7,
                        0.0,
                        (world.random.nextDouble() - 0.5) * 1.7,
                        0.0
                    )
                }
            }
            stack.set(AstralItemComponents.JUMP_DATA, Data(data.uses - 1, data.cooldown, 0, data.maxUses - 1))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super<AirSpeedKosmogliph>.inventoryTick(stack, world, entity, slot, selected)
        if (slot == 0) {
            val data = stack.get(AstralItemComponents.JUMP_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var uses = data.uses
            var lastJump = data.lastJump
            var maxUses = data.maxUses
            if (uses >= 3) return
            var cooldown = data.cooldown
            if (entity.isOnGround || entity.isInFluid) maxUses = 3
            if (entity is PlayerEntity) {
                if (entity.hungerManager.foodLevel > 6) {
                    if (uses < maxUses) cooldown--
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
                    val z: Int = (entity.frozenTicks / 20) * 5
                    time += z
                }
                cooldown = time
                if (entity is ServerPlayerEntity) {
                    entity.networkHandler.send(
                        SoundPlayS2CPacket(
                            Holder.createDirect(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL),
                            SoundCategory.PLAYERS,
                            entity.x,
                            entity.y,
                            entity.z,
                            0.6F,
                            x,
                            world.getRandom().nextLong()
                        )
                    )
                }
            }

            if (lastJump < 20) lastJump++

            stack.set(AstralItemComponents.JUMP_DATA, Data(uses, cooldown, lastJump, maxUses))
        }
    }

    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        val data = stack.get(AstralItemComponents.JUMP_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = data.cooldown
        var maxUses = data.maxUses
        if (damage >= 2 && !source.isTypeIn(AstralDamageTypeTags.KEEPS_MOVEMENT)) {
            if (uses >= 3) {
                uses += -1
                cooldown += 20
                maxUses += -1
            } else if (cooldown >= 100 && uses != 0) {
                uses += -1
                maxUses += -1
            } else if (cooldown <= 100) {
                cooldown += 10
            }
        }
        stack.set(AstralItemComponents.JUMP_DATA, Data(uses, cooldown, 0, maxUses))
        return super<SimpleKosmogliph>.modifyDamage(stack, entity, damage, source, equipmentSlot)
    }

    data class Data(
        val uses: Int,
        val cooldown: Int,
        val lastJump: Int,
        val maxUses: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0], list[1], list.getOrNull(2) ?: 0, list[3]) },
                    { data -> listOf(data.uses, data.cooldown, data.lastJump, data.maxUses) }
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