package org.teamvoided.astralarsenal.item

import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials

class AstralGreathammerItem(settings: Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {

    override fun isEnchantable(stack: ItemStack?): Boolean {
        return true
    }
    // I need someone to take all the hitTimes logic, apply it to a kosmogliph,
    // and make it work per itemstack pls thx <3
}