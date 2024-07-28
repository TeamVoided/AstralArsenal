package org.teamvoided.astralarsenal.item.kosmogliph.logic

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.util.PlayerInteractionManagerExtension
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack
import kotlin.math.min


fun hammerTryBeakBlocks(world: World, player: PlayerEntity, pos: BlockPos): Boolean {
    if (world.isClient) return false
    if (player.isSneaking) return false

    val interactionManager = (player as ServerPlayerEntity).interactionManager as PlayerInteractionManagerExtension
    interactionManager.kosmogliph_setMining(true)

    val stack = player.mainHandStack
    val state = world.getBlockState(pos)

    val mineablePositions = queryMineableHammerPositions(stack, world, pos, state, player)
    mineablePositions.breakAndDropStacksAt(world, pos, player)
    return if (mineablePositions.isEmpty()) false else {
        stack.damageEquipment(mineablePositions.size, player, EquipmentSlot.MAINHAND)
        true
    }
}


fun queryMineablePositions(
    stack: ItemStack, world: World, pos: BlockPos, state: BlockState, miner: LivingEntity
): Set<BlockPos> {
    val kosmogliph = getKosmogliphsOnStack(stack)
    if (kosmogliph.contains(AstralKosmogliphs.HAMMER)) queryMineableHammerPositions(stack, world, pos, state, miner)
    if (kosmogliph.contains(AstralKosmogliphs.VEIN_MINER)) queryMineableVeinPositions(
        stack, world, state, pos, 30.0, min(64, stack.maxDamage - stack.damage)
    )

    return setOf()
}


fun queryMineableVeinPositions(
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

fun queryMineableHammerPositions(
    stack: ItemStack, world: World, pos: BlockPos, mainState: BlockState, miner: LivingEntity
): Set<BlockPos> {
    val minablePositions = mutableSetOf(pos)
    val reach = if (miner.isInCreativeMode) 5.0 else 4.5

    val combined = miner.eyePos.add(miner.rotationVector.multiply(reach))

    //raycast always return a BlockHitResult
    val raycast = world.raycast(
        RaycastContext(
            miner.eyePos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, miner
        )
    )

    val aoe = areaOfAffect(pos, raycast.side)

    aoe.allInside().forEach {
        val state = world.getBlockState(it)
        if (stack.canSafelyBreak(world, state, it)) {
            if (mainState.getHardness(world, pos) + 0.1 >= state.getHardness(world, it)) {
                minablePositions.add(it)
            }
        }
    }

    val durability = stack.maxDamage - stack.damage
    if (durability < minablePositions.size) {
        return minablePositions.toList().subList(0, durability - 1).toSet()
    }

    return minablePositions
}

fun BlockBox.allInside(): Set<BlockPos> {
    val positions = mutableSetOf<BlockPos>()

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                positions.add(BlockPos(x, y, z))
            }
        }
    }

    return positions
}

fun areaOfAffect(pos: BlockPos, direction: Direction): BlockBox {
    return when (direction) {
        Direction.DOWN, Direction.UP -> BlockBox(pos.x - 1, pos.y, pos.z - 1, pos.x + 1, pos.y, pos.z + 1)
        Direction.NORTH, Direction.SOUTH -> BlockBox(pos.x - 1, pos.y - 1, pos.z, pos.x + 1, pos.y + 1, pos.z)
        Direction.EAST, Direction.WEST -> BlockBox(pos.x, pos.y - 1, pos.z - 1, pos.x, pos.y + 1, pos.z + 1)
    }
}

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
    }.flatten() //.combined()

    combined.forEach { stack -> Block.dropStack(world, pos, stack) }
}

// causes item dupe
fun List<ItemStack>.combined(): List<ItemStack> {
    val combined = mutableListOf<ItemStack>()
    map { it.copy() }.forEach { stack ->
        val expandable = combined.filter { it.count < it.maxCount }
        if (expandable.isEmpty()) {
            combined.add(stack.copy())
            return@forEach
        }

        expandable.forEach expand@{ other ->
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
