package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralSounds
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import java.lang.Math.random

class JumpKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.BOOTS
}) {
    // Change this to change how much boost is given when double-jumping.
    val JUMP_FORWARD_BOOST = 0.3

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.JUMP_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world

        if(data.uses > 0 && !player.isOnGround) {
            var boost = player.rotationVector.multiply(0.0,0.0,0.0)
            if(player.velocity.x > 0.0 || player.velocity.z > 0.0){boost = player.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(JUMP_FORWARD_BOOST)}
            player.setVelocity(player.velocity.x + boost.x,0.5, player.velocity.z + boost.z)
            player.velocityModified = true
            player.hungerManager.add(0,-0.1f)
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_ROOTED_DIRT_PLACE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F)
            if(!world.isClient){
                val serverWorld = world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.SPIT,
                    player.x,
                    player.y - 1,
                    player.z,
                    20,
                    random().minus(0.5).times(2),
                    0.0,
                    random().minus(0.5).times(2),
                    0.0)
            }
            stack.set(AstralItemComponents.JUMP_DATA, Data(data.uses - 1, data.cooldown, 0, data.maxUses - 1))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.JUMP_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var lastJump = data.lastJump
        var maxUses = data.maxUses
        if (uses >= 3) return
        var cooldown = data.cooldown
        if(entity.isOnGround) maxUses = 3
        if(uses < maxUses) cooldown--

        if (cooldown <= 0) {
            uses++
            val x : Float = (uses * 0.5).toFloat()
            cooldown = 20
            world.playSound(
                null,
                entity.x,
                entity.y,
                entity.z,
                AstralSounds.CHARGE,
                SoundCategory.PLAYERS,
                1.0F,
                x)
        }

        if (lastJump < 20) lastJump++

        stack.set(AstralItemComponents.JUMP_DATA, Data(uses, cooldown, lastJump, maxUses))
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
}