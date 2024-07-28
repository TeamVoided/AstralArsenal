package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.breakAndDropStacksAt
import org.teamvoided.astralarsenal.item.kosmogliph.logic.queryMineableHammerPositions

class HammerKosmogliph(id: Identifier) : SimpleKosmogliph(id,
    { it.item is PickaxeItem || it.item is ShovelItem || it.item is AxeItem || it.item is HoeItem }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return
        if (miner.isSneaking) return

        val mineablePositions = queryMineableHammerPositions(stack, world, pos, state, miner)
        mineablePositions.breakAndDropStacksAt(world, pos, miner)

        stack.damageEquipment(mineablePositions.size - 1, miner, EquipmentSlot.MAINHAND)
    }


    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf()
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}