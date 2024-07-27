package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.breakAndDropStacksAt
import org.teamvoided.astralarsenal.item.kosmogliph.logic.canSafelyBreak

class VeinmineKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.item is PickaxeItem }) {
    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) {
        if (world.isClient() || world !is ServerWorld) return
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
                            stack.canSafelyBreak(world, state, pos) &&
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