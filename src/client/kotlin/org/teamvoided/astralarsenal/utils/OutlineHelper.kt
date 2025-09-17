package org.teamvoided.astralarsenal.utils

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BlockBreakingInfo
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.data.tags.AstralBlockTags.REAPABLE_CROPS
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HAMMER
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.REAPER
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.VEIN_MINER
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.kosmogliph.logic.*
import org.teamvoided.astralarsenal.util.TillingActions
import org.teamvoided.astralarsenal.util.VEINMINE_RANGE
import org.teamvoided.astralarsenal.util.VEINMINE_VOLUME
import org.teamvoided.astralarsenal.util.getKosmogliphs
import java.util.*
import kotlin.math.min


// this code is a modified version fabric-hammers mod
// https://github.com/bdani0717/fabric-hammers-1.20
fun getPositions(client: MinecraftClient): Set<BlockPos> {
    val player = client.player ?: return setOf()
    val world = client.world ?: return setOf()

    if (player.isSneaking) return setOf()

    val stack = player.mainHandStack
    val comp = stack.getKosmogliphs()
    if (comp.isEmpty()) return setOf()

    val glyph = comp.changesMiningShape() ?: return setOf()

    if (client.crosshairTarget !is BlockHitResult) return setOf()

    val pos: BlockPos = (client.crosshairTarget as BlockHitResult).blockPos
    val state = client.world!!.getBlockState(pos)
    if (!state.isAir && client.world!!.worldBorder.contains(pos)) return glyph.getBlocks(
        stack,
        world,
        pos,
        state,
        player
    )

    return setOf()
}

fun getOutlineShape(client: MinecraftClient): Pair<VoxelShape, BlockPos>? {
    val positions = getPositions(client)
    if (positions.size < 2) return null
    val world = client.world ?: return null
    val player = client.player ?: return null
    val pos: BlockPos = (client.crosshairTarget as BlockHitResult).blockPos

    var outlineShape = VoxelShapes.empty()
    for (position in positions) {
        val diffPos = position.subtract(pos)
        val offsetBlock = world.getBlockState(position)
        if (!offsetBlock.isAir) outlineShape = VoxelShapes.union(
            outlineShape,
            offsetBlock.getOutlineShape(world, position, ShapeContext.of(player))
                .offset(diffPos.x.toDouble(), diffPos.y.toDouble(), diffPos.z.toDouble())
        )
    }
    return outlineShape to pos
}

fun addExtraBreakingInfo(
    client: MinecraftClient, positions: MutableSet<BlockPos>,
    blockBreakingProgressions: Long2ObjectMap<SortedSet<BlockBreakingInfo>>,
) {
    if (client.crosshairTarget !is BlockHitResult) return
    val mainPos = (client.crosshairTarget as BlockHitResult).blockPos
    val breakingPos = positions.filter { it != mainPos }
    val info = blockBreakingProgressions.get(mainPos.asLong())
    if (info == null || info.isEmpty()) return

    if (shouldReaperHaveBreaking(client, mainPos)) return

    for (pos in breakingPos) blockBreakingProgressions.put(pos.asLong(), info)
}

fun shouldReaperHaveBreaking(client: MinecraftClient, mainPos: BlockPos): Boolean {
    val player = client.player ?: return false
    val world = client.world ?: return false

    if (player.isSneaking) return false

    val stack = player.mainHandStack
    val comp = stack.getKosmogliphs()
    if (comp.isEmpty()) return false

    if (!comp.contains(REAPER)) return false

    return !world.getBlockState(mainPos).isIn(REAPABLE_CROPS)
}


fun KosmogliphsComponent.changesMiningShape(): SimpleKosmogliph? = if (this.contains(HAMMER)) HAMMER
else if (this.contains(REAPER)) REAPER
else if (this.contains(VEIN_MINER)) VEIN_MINER
else null

fun Kosmogliph.getBlocks(
    stack: ItemStack, world: World, pos: BlockPos, state: BlockState, player: PlayerEntity,
): Set<BlockPos> {
    return when (this) {
        HAMMER -> queryMineableHammerPositions(stack, world, pos, state, player)
        REAPER -> queryReaperOutline(stack, world, pos, state, player)
        VEIN_MINER -> queryMineableVeinPositions(
            stack, world, state, pos, VEINMINE_RANGE, min(VEINMINE_VOLUME, stack.maxDamage - stack.damage)
        )

        else -> emptySet()
    }
}

fun queryReaperOutline(
    stack: ItemStack, world: World, pos: BlockPos, state: BlockState, player: PlayerEntity,
): Set<BlockPos> {
    val mineable = queryReaperMineablePositions(stack, world, pos, state)
    if (mineable.size > 1) return mineable
    val tillable = queryReaperTillable(pos, world, player)
    if (mineable.isEmpty() && tillable.size > 1) return tillable
    return setOf()
}

fun queryReaperTillable(pos: BlockPos, world: World, miner: PlayerEntity): Set<BlockPos> {
    val castEnd = miner.eyePos.add(miner.rotationVector.multiply(miner.reach()))
    val raycast = world.raycast(
        RaycastContext(
            miner.eyePos, castEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, miner
        )
    )

    return areaOfAffect(pos, raycast.side).allInside().filter {
        if (!it.isInWorld(world)) return@filter false
        val predicate = TillingActions.map[world.getBlockState(it).block] ?: return@filter false
        predicate.first.test(ItemUsageContext(miner, miner.activeHand, raycast))
    }.toSet()

}
