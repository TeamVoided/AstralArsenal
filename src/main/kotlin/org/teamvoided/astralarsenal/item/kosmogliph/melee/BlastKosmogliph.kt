package org.teamvoided.astralarsenal.item.kosmogliph.melee

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BoomEntity
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph


class BlastKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.item is SwordItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        if (!world.isClient) {
            for (i in 1..10){
            val snowballEntity = BoomEntity(world, player)
            snowballEntity.setProperties(player,
                player.pitch.plus(world.random.nextDouble().minus(0.5).times(10)).toFloat(),
                player.yaw.plus(world.random.nextDouble().minus(0.5).times(60)).toFloat(),
                0.0f,
                1f,
                0.0f
            )
            snowballEntity.addVelocity(0.0, 0.0, 0.0)
            world.spawnEntity(snowballEntity)
            if (!player.isCreative) {
                player.itemCooldownManager.set(player.getStackInHand(hand).item, 100)
            }
        }
        }
    }
}