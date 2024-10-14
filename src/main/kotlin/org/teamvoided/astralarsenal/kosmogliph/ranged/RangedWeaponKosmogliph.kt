package org.teamvoided.astralarsenal.kosmogliph.ranged

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand

interface RangedWeaponKosmogliph {
    fun preFire(
        world: ServerWorld,
        user: LivingEntity,
        hand: Hand,
        stack: ItemStack,
        projectiles: List<ItemStack>,
        speed: Float,
        divergence: Float,
        isPlayer: Boolean,
        entity: LivingEntity?,
    ): Boolean = false
}