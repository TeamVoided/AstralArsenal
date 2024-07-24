package org.teamvoided.astralarsenal.item.kosmogliph.ranged

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
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

    fun overrideArrowType(player: PlayerEntity, stack: ItemStack): ItemStack? = null
}