package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.RailgunKosmogliph
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

class RailgunItem(settings: Settings) : Item(settings) {
    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        getKosmogliphsOnStack(stack).forEach { (it as? RailgunKosmogliph)?.shoot(stack, world, user) }
        println("balls")
        return stack
    }

    override fun isUsedOnRelease(stack: ItemStack): Boolean {
        return stack.isOf(this)
    }

    override fun getUseTicks(stack: ItemStack, entity: LivingEntity): Int {
        return 20
    }
}