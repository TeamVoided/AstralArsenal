package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
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
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.DashKosmogliph.Data
import java.lang.Math.random
import kotlin.math.sqrt

class DodgeKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.LEGGINGS
}), AirSpeedKosmogliph {
    val JUMP_FORWARD_BOOST = 5.0
    val SPEED_CAP = 1.0
    val SPEED_MULT = sqrt(2 * SPEED_CAP * SPEED_CAP)

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.DODGE_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world
        //this is broken and I don't know why, please help
        if(data.uses > 0 && !world.isClient) {
            val boost = player.movement.multiply(1.0, 0.0, 1.0).multiply(JUMP_FORWARD_BOOST)
            player.addVelocity(boost)
            if(player.velocity.horizontalLength() > SPEED_CAP)
                player.velocity = boost.normalize().multiply(SPEED_MULT)

            player.velocityModified = true
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                AstralSounds.DODGE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F)
            if(!world.isClient){
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    player.x,
                    player.y,
                    player.z,
                    20,
                    random().minus(0.5).times(2),
                    random().minus(0.5).times(2),
                    random().minus(0.5).times(2),
                    0.0)
            }
            stack.set(AstralItemComponents.DODGE_DATA, Data(data.uses - 1, data.cooldown))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super<AirSpeedKosmogliph>.inventoryTick(stack, world, entity, slot, selected)
        if ((entity is PlayerEntity) && entity.inventory.armor.contains(stack) && slot == 1) {
            val data = stack.get(AstralItemComponents.DODGE_DATA)
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
                    val z: Int = (entity.frozenTicks/20) * 5
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

            stack.set(AstralItemComponents.DODGE_DATA, Data(uses, cooldown))
        }
    }
    override fun modifyDamage(
        stack: ItemStack,
        entity: LivingEntity,
        damage: Float,
        source: DamageSource,
        equipmentSlot: EquipmentSlot
    ): Float {
        val data = stack.get(AstralItemComponents.DODGE_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var cooldown = data.cooldown
        if(damage >= 2){
        if (uses >= 3){
            uses += -1
            cooldown += 20
        }
        else{
            cooldown += 5
        }}
        stack.set(AstralItemComponents.DODGE_DATA,
            Data(uses, cooldown)
        )
        return super<SimpleKosmogliph>.modifyDamage(stack, entity, damage, source, equipmentSlot)
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