package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.lang.Math.random

class DodgeKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.LEGGINGS
}) {
    // change this to change how much boost they get :3
    val JUMP_FORWARD_BOOST = 1.0

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.DODGE_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world
        println("velX: " + player.velocity.x + " velZ: " + player.velocity.z)
        //this is broken and I don't know why, please help
        if(data.uses > 0 && !world.isClient) {
            val boost = player.velocity.multiply(1.0, 0.0, 1.0).multiply(JUMP_FORWARD_BOOST)
            println("velX: " + player.velocity.x + " velZ: " + player.velocity.z + " boostX: " + boost.x + " boost z: " + boost.z)
            player.setVelocity(player.velocity.x + boost.x, 0.1, player.velocity.z + boost.z)
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
        if (slot == 2) {
            val data = stack.get(AstralItemComponents.DODGE_DATA)
                ?: throw IllegalStateException("Erm, how the fuck did you manage this")
            var uses = data.uses
            if (uses >= 3) return
            var cooldown = data.cooldown
            cooldown--

            if (cooldown <= 0) {
                uses++
                val x: Float = (uses * 0.5).toFloat()
                cooldown = 20
                world.playSound(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    AstralSounds.CHARGE,
                    SoundCategory.PLAYERS,
                    1.0F,
                    x
                )
            }

            stack.set(AstralItemComponents.DODGE_DATA, Data(uses, cooldown))
        }
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
}