package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class CannonballLauncherKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is CrossbowItem }) {
    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        super.onUse(world, player, hand)
    }
}