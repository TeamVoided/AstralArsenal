package org.teamvoided.astralarsenal.item.kosmogliph

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents

// I need some help changing this into a boots kosmogliph that constantly ticks the cooldowns and does
// the extra jump functionality when space is clicked.

class JumpKosmogliph(id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.BOOTS
}) {
    // Change this to change how much boost is given when double-jumping.
    val JUMP_FORWARD_BOOST = 0.3

    fun handleJump(stack: ItemStack, player: PlayerEntity) {
        val data = stack.get(AstralItemComponents.JUMP_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        val world = player.world

        if(data.uses > 0 && !player.isOnGround && data.lastJump >= 10) {
            val boost = player.rotationVector.multiply(1.0, 0.0, 1.0).normalize().multiply(JUMP_FORWARD_BOOST)
            player.setVelocity(player.velocity.x + boost.x,0.5, player.velocity.z + boost.z)
            player.velocityModified = true
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_ROOTED_DIRT_PLACE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F)
            stack.set(AstralItemComponents.JUMP_DATA, Data(data.uses - 1, data.cooldown, 0))
        }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.JUMP_DATA) ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        var lastJump = data.lastJump
        if (uses >= 3) return
        var cooldown = data.cooldown
        if(entity.isOnGround) cooldown--

        if (cooldown <= 0) {
            uses++
            cooldown = 30
        }

        if (lastJump < 20) lastJump++

        stack.set(AstralItemComponents.JUMP_DATA, Data(uses, cooldown, lastJump))
    }

    data class Data(
        val uses: Int,
        val cooldown: Int,
        val lastJump: Int
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0], list[1], list.getOrNull(2) ?: 0) },
                    { data -> listOf(data.uses, data.cooldown, data.lastJump) }
                )
        }
    }
}