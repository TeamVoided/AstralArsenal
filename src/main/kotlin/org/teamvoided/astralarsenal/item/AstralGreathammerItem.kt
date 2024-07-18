package org.teamvoided.astralarsenal.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.entity.CannonballEntity

class AstralGreathammerItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings) {

    override fun isEnchantable(stack: ItemStack?): Boolean {
        return true
    }
    // I need someone to take all the hitTimes logic, apply it to a kosmogliph,
    // and make it work per itemstack pls thx <3
}