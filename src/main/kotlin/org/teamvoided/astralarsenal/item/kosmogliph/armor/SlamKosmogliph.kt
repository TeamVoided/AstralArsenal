package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class SlamKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.HELMET
}) {
    fun handleSlam(stack: ItemStack, player: PlayerEntity) {

        if(!player.isOnGround) {
            player.setVelocity(0.0,-100.0, 0.0)
            player.velocityModified = true
        }

    }
}
//Gonna need a lot of help adding the rest of the functionality for this.
//that functionality is: for every block fallen after pressing crouch, give the player x ammount of jump boost
//for a split second, and create a knockback explosion that ignores the player who slammed.