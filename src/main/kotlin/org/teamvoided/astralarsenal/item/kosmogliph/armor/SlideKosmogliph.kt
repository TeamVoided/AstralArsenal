package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.lang.Math.random

class SlideKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.LEGGINGS
}) {
    // Change this to change the maximum speed
    val JUMP_FORWARD_BOOST = 1.0

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.DASH_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world

        if(player.isOnGround) {
            val boost = player.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(JUMP_FORWARD_BOOST)
            player.setVelocity((player.velocity.x + boost.x).coerceIn(-1.0,1.0),0.0, (player.velocity.z + boost.z).coerceIn(-1.0,1.0))
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
        }
    }
}