package org.teamvoided.astralarsenal.item.kosmogliph.logic

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun Set<BlockPos>.breakAndDropStacksAt(
    world: World,
    pos: BlockPos,
    miner: LivingEntity
) {
    forEach { otherPos ->
        if (otherPos != pos) {
            val stacks = Block.getDroppedStacks(
                world.getBlockState(otherPos),
                world as ServerWorld,
                otherPos,
                world.getBlockEntity(otherPos),
                miner,
                miner.mainHandStack
            )
            stacks.forEach { stack -> Block.dropStack(world, pos, stack) }
        }
        world.breakBlock(otherPos, false, miner)
    }
}

fun Item.canSafelyBreak(world: World, stack: ItemStack, state: BlockState, pos: BlockPos): Boolean {
    val speed = getMiningSpeedMultiplier(stack, state)
    return speed >= 0 && world.getBlockEntity(pos) == null
}
