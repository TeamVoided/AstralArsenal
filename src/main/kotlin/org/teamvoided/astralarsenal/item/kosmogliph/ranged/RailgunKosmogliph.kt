package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

interface RailgunKosmogliph {
    fun shoot(stack: ItemStack, world: World, user: LivingEntity)
}