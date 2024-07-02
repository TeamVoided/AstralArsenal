package org.teamvoided.astralarsenal.item.kosmogliph.logic

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.teamvoided.astralarsenal.block.tag.AstralBlockTags
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralKosmogliphs

object VeinmineKosmogliphLogic {
    fun veinMine(
        stack: ItemStack,
        world: World,
        state: BlockState,
        pos: BlockPos,
        miner: LivingEntity
    ) {
        if (world.isClient() || world !is ServerWorld) return
        if (!stack.components.contains(AstralItemComponents.KOSMOGLIPHS)) return
        val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS)!!
        if (!kosmogliphs.contains(AstralKosmogliphs.VEIN_MINER)) return

        val mineablePositions =
            queryMinablePositions(stack, world, state, pos, 30.0, (64).coerceAtMost(stack.maxDamage - stack.damage))
        mineablePositions.breakAndDropStacksAt(world, pos, miner)

        stack.damageEquipment(mineablePositions.size, miner, EquipmentSlot.MAINHAND)
    }

    fun queryMinablePositions(
        stack: ItemStack,
        world: World,
        state: BlockState,
        pos: BlockPos,
        maximumDistance: Double,
        maximumBlocks: Int
    ): Set<BlockPos> {
        val set = mutableSetOf<BlockPos>()
        val queue = ArrayDeque<BlockPos>()
        queue.add(pos)

        while (queue.isNotEmpty()) {
            val newPos = queue.removeFirst()
            val block = state.block
            val neighbors = newPos.neighbors()
                .filter {
                    val itState = world.getBlockState(it)
                    itState.isOf(block) &&
                            itState.isIn(AstralBlockTags.VEIN_MINEABLE) &&
                            stack.canSafelyBreak(world, state, pos)
                            it.isWithinDistance(pos, maximumDistance) &&
                            !set.contains(it)
                }

            if (set.size + neighbors.size >= maximumBlocks) {
                val difference = maximumBlocks - set.size
                if (difference == 0) break
                set.addAll(neighbors.subList(0, difference - 1))
                break
            }

            queue.addAll(neighbors)
            set.addAll(neighbors)
        }

        return set
    }

    fun BlockPos.neighbors(): Set<BlockPos> {
        return Direction.entries.map { this.offset(it) }.toSet()
    }
}