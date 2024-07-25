package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EntityPose
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.lang.Math.random

class SlideKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.LEGGINGS
}) {
    // removed
    val JUMP_FORWARD_BOOST = 1.0

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val world = player.world
        if (world.isClient) return

        if(player.isOnGround) {
            val boost = player.movement.multiply(1.0, 0.0, 1.0).normalize().multiply(JUMP_FORWARD_BOOST)
            player.setVelocity((player.movement.x + boost.x).coerceIn(-1.0,1.0),0.0, (player.movement.z + boost.z).coerceIn(-1.0,1.0))
            player.velocityModified = true
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_HEAVY_CORE_STEP,
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
                    1,
                    random().minus(0.5).times(2),
                    random().minus(0.5).times(2),
                    random().minus(0.5).times(2),
                    0.1)
            }
            player.pose = EntityPose.SLIDING
        }
    }
    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.THORNS)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}