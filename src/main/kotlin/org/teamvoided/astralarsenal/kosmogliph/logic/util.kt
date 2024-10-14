package org.teamvoided.astralarsenal.kosmogliph.logic

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CropBlock
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags.REAPABLE_CROPS
import org.teamvoided.astralarsenal.util.PlayerInteractionManagerExtension


fun hammerTryBeakBlocks(world: World, player: PlayerEntity, pos: BlockPos): Boolean {
    if (world.isClient) return false
    if (player.isSneaking) return false

    val stack = player.mainHandStack
    val state = world.getBlockState(pos)

    if (!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, pos, state, world.getBlockEntity(pos)))
        return false


    val interactionManager = (player as ServerPlayerEntity).interactionManager as PlayerInteractionManagerExtension
    interactionManager.kosmogliph_setMining(true)


    val mineablePositions = queryMineableHammerPositions(stack, world, pos, state, player)

    mineablePositions.breakAndDropStacksAt(world, pos, player, stack)
    val hadPositions = if (mineablePositions.isEmpty()) false else {
        stack.damageEquipment(mineablePositions.size, player, EquipmentSlot.MAINHAND)
        true
    }
    interactionManager.kosmogliph_setMining(false)
    return hadPositions
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
                        stack.canSafelyBreak(world, itState, it) &&
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
    val set = mutableSetOf<BlockPos>()
    for (i in -1..1) {
        for (j in -1..1) {
            for (k in -1..1) {
                if (i == 0 && j == 0 && k == 0) continue
                set.add(this.add(i, j, k))
            }
        }
    }
    return set
//    return Direction.entries.map { this.offset(it) }.toSet()
}

fun LivingEntity.reach() = if (this.isInCreativeMode) 5.0 else 4.5

fun queryReaperMineablePositions(tool: ItemStack, world: World, pos: BlockPos, mainState: BlockState): Set<BlockPos> {
    val isMatureMain = mainState.isMature() ?: true
    if (!mainState.isIn(REAPABLE_CROPS) || !isMatureMain) return setOf()

    val mineablePositions = mutableSetOf<BlockPos>()

    val aoe = BlockBox(pos.x - 1, pos.y - 1, pos.z - 1, pos.x + 1, pos.y + 1, pos.z + 1).allInside()
    aoe.forEach {
        val state = world.getBlockState(it)
        val isMature = state.isMature() ?: true
        if (pos.isInWorld(world) && state.isIn(REAPABLE_CROPS) && isMature) mineablePositions.add(it)
    }

    val durability = tool.maxDamage - tool.damage
    if (durability < mineablePositions.size) {
        return mineablePositions.toList().subList(0, durability - 1).toSet()
    }

    return mineablePositions
}

fun queryMineableHammerPositions(
    stack: ItemStack, world: World, pos: BlockPos, mainState: BlockState, miner: LivingEntity
): Set<BlockPos> {
    if (stack.get(DataComponentTypes.TOOL)?.isCorrectForDrops(world.getBlockState(pos)) != true) return setOf()

    val mineablePositions = mutableSetOf(pos)
    val reach = if (miner.isInCreativeMode) 5.0 else 4.5

    val combined = miner.eyePos.add(miner.rotationVector.multiply(reach))

    val raycast = world.raycast(
        RaycastContext(
            miner.eyePos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, miner
        )
    )

    val aoe = areaOfAffect(pos, raycast.side)

    aoe.allInside().forEach {
        val state = world.getBlockState(it)
        if (stack.canSafelyBreak(world, state, it)) {
            val player = miner as PlayerEntity
            val base = mainState.calcBlockBreakingDelta(player, world, pos)
            val side = state.calcBlockBreakingDelta(player, world, it)
            if (side > 1.0 || base <= side) {
                mineablePositions.add(it)
            }
        }
    }

    val durability = stack.maxDamage - stack.damage
    if (durability < mineablePositions.size) {
        return mineablePositions.toList().subList(0, durability - 1).toSet()
    }

    return mineablePositions
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
    world: World, pos: BlockPos, miner: LivingEntity, stack: ItemStack
) {
    val combined = map { otherPos ->
        val state = world.getBlockState(otherPos)
        val stacks =
            Block.getDroppedStacks(state, world as ServerWorld, otherPos, world.getBlockEntity(otherPos), miner, stack)

        state.onStacksDropped(world, otherPos, stack, true)
        world.breakBlock(otherPos, false, miner)
        if (miner is PlayerEntity && !miner.isCreative) miner.incrementStat(Stats.MINED.getOrCreateStat(state.block))
        stacks
    }.flatten() //.combined()

    if (miner is ServerPlayerEntity && miner.isCreative) return
    combined.forEach { droppedStack -> Block.dropStack(world, pos, droppedStack) }
}

// causes item dupe
@Suppress("unused")
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
    return pos.isInWorld(world) && toolComponent.isCorrectForDrops(state) && world.getBlockEntity(pos) == null
}

fun BlockPos.isInWorld(world: World): Boolean = world.worldBorder.contains(this)
fun BlockState.isMature(): Boolean? = if (this.block is CropBlock) (this.block as CropBlock).isMature(this) else null


// Ranged Weapon
fun World.getProjectileEntity(
    projectileStack: ItemStack, weaponStack: ItemStack, player: PlayerEntity, canPickUp: Boolean
): ProjectileEntity {
    return if (projectileStack.isOf(Items.FIREWORK_ROCKET)) {
        FireworkRocketEntity(
            this, projectileStack, player, player.x, player.eyeY - 0.15f.toDouble(), player.z, true
        )
    } else {
        val arrowEntity = ArrowEntity(this, player, projectileStack, weaponStack)
        arrowEntity.isCritical = true
        arrowEntity.pickupType = if (canPickUp) PersistentProjectileEntity.PickupPermission.ALLOWED
        else PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY

        arrowEntity
    }
}

//(ender) IDK what this actually does this is just a guess
fun ProjectileEntity.setShootVelocity(pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float) {
    val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
    val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    this.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
}
