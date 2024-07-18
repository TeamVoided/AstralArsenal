package org.teamvoided.astralarsenal.item.kosmogliph

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.CannonballEntity
import org.teamvoided.astralarsenal.item.AstralGreathammerItem

class CannonballKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is AstralGreathammerItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            val snowballEntity = CannonballEntity(world, player)
            snowballEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 0.1f, 0.0f)
            snowballEntity.addVelocity(0.0, 0.1, 0.0)
            world.spawnEntity(snowballEntity)
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
            }
        }
    }
}