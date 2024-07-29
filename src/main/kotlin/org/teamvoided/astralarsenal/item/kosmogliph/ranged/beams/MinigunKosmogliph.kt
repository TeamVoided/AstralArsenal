package org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.armor.DashKosmogliph.Data

class MinigunKosmogliph (id: Identifier) :
    SimpleKosmogliph(id, { it.item is RailgunItem }){
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.get(AstralItemComponents.MINIGUN_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
        var uses = data.uses
        if(uses >= 50) return
        var cooldown = data.cooldown
        cooldown--
        if(cooldown <= 0){
            uses++
            cooldown = 10
        }

        stack.set(AstralItemComponents.MINIGUN_DATA,
            org.teamvoided.astralarsenal.item.kosmogliph.ranged.beams.MinigunKosmogliph.Data(uses, cooldown)
        )
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun onUse(world: World, player: PlayerEntity, hand: Hand) {
        val stack: ItemStack
        super.onUse(world, player, hand)
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