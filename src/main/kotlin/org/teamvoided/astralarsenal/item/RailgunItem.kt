package org.teamvoided.astralarsenal.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class RailgunItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return super.use(world, user, hand)
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return UseAction.CROSSBOW
    }
}