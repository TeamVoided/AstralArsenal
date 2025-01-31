package org.teamvoided.astralarsenal.kosmogliph.ranged.beams

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.MinigunData
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.item.RailgunItem
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData

//This needs to be moved to a crossbow kosmogliph
class MinigunKosmogliph(id: Identifier) : KosmogliphWithData(id, AstralDataComponents.MINIGUN_DATA, { it.item is RailgunItem }) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(AstralDataComponents.MINIGUN_DATA, MinigunData.DEFAULT)
        var uses = data.uses
        if (uses >= 50) return
        var cooldown = data.cooldown
        cooldown--
        if (cooldown <= 0) {
            uses++
            cooldown = 10
        }

        stack.set(
            AstralDataComponents.MINIGUN_DATA,
            MinigunData(uses, cooldown)
        )
        super.inventoryTick(stack, world, entity, slot, selected)
    }

}