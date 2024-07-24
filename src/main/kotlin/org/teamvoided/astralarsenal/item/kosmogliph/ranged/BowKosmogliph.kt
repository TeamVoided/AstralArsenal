package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

interface BowKosmogliph : RangedWeaponKosmogliph {
    fun overrideArrowType(player: PlayerEntity, stack: ItemStack, original: ItemStack): ItemStack? = null
}