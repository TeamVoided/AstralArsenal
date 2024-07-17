package org.teamvoided.astralarsenal.item.kosmogliph

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World

// I need some help changing this into a boots kosmogliph that constantly ticks the cooldowns and does
// the extra jump functionality when space is clicked.

class JumpKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is ArmorItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if(offCooldown > 0 && !player.isOnGround){
            player.setVelocity(player.velocity.x,0.5,player.velocity.z)
            world.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLOCK_ROOTED_DIRT_PLACE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F)
            offCooldown --
            maxUses --
        }
        super.onUse(world, player, hand)
    }
    var maxUses = 3
    var offCooldown = 0
    var cooldown = 30
    var cooling = 0

    //I know tick here does nothing, but i need it to do something and IDK how
    fun tick(player: PlayerEntity){
        if(player.isOnGround){
            maxUses = 3
        }
        cooling ++
        if(cooling == cooldown && offCooldown > maxUses){
            cooling = 0
            offCooldown ++
        }
    }
}