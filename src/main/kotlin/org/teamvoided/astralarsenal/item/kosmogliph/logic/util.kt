package org.teamvoided.astralarsenal.item.kosmogliph.logic

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun Set<BlockPos>.breakAndDropStacksAt(
    world: World,
    pos: BlockPos,
    miner: LivingEntity
) {
    val combined = map { otherPos ->
        val stacks = Block.getDroppedStacks(
            world.getBlockState(otherPos),
            world as ServerWorld,
            otherPos,
            world.getBlockEntity(otherPos),
            miner,
            miner.mainHandStack
        )

        world.breakBlock(otherPos, false, miner)
        stacks
    }.flatten().combined()

    combined.forEach { stack -> Block.dropStack(world, pos, stack) }
}

fun List<ItemStack>.combined(): List<ItemStack> {
    val combined = mutableListOf<ItemStack>()
    map { it.copy() }.forEach { stack ->
        val expandable = combined.filter { it.count < it.maxCount }
        if (expandable.isEmpty()) {
            combined.add(stack.copy())
            return@forEach
        }

        expandable.forEach expand@ { other ->
            if (stack.isEmpty) return@expand
            val addable = (other.maxCount - other.count).coerceAtMost(stack.count)
            other.count += addable
            stack.count -= addable
        }

        if (!stack.isEmpty) combined.add(stack.copy())
    }
    return combined
}

fun ItemStack.canSafelyBreak(world: World, state: BlockState, pos: BlockPos): Boolean {
    val toolComponent = this.get(DataComponentTypes.TOOL) ?: return false
    return toolComponent.isCorrectForDrops(state) && world.getBlockEntity(pos) == null
}
