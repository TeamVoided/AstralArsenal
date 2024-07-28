package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.breakAndDropStacksAt
import org.teamvoided.astralarsenal.item.kosmogliph.logic.queryMineableVeinPositions

class VeinmineKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is PickaxeItem }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return
        if (miner.isSneaking) return
        val mineablePositions =
            queryMineableVeinPositions(stack, world, state, pos, 30.0, (64).coerceAtMost(stack.maxDamage - stack.damage))
        mineablePositions.breakAndDropStacksAt(world, pos, miner, stack)

        stack.damageEquipment(mineablePositions.size - 1, miner, EquipmentSlot.MAINHAND)
    }
}