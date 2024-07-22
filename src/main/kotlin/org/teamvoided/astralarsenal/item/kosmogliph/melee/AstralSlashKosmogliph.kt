package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.SlashEntity
import org.teamvoided.astralarsenal.item.AstralGreathammerItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class AstralSlashKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem && it.item !is AstralGreathammerItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            var w = -20
            repeat(20){
                val snowballEntity = SlashEntity(world, player)
                snowballEntity.setDmg(10f)
                snowballEntity.setProperties(player, player.pitch, player.yaw + w, 0.0f, 2.0f, 0.0f)
                world.spawnEntity(snowballEntity)
                w += 2
            }
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 200)
            }
        }
    }
}