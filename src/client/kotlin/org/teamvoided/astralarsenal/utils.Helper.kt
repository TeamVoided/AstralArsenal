package org.teamvoided.astralarsenal

import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HAMMER
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.REAPER
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.VEIN_MINER
import org.teamvoided.astralarsenal.item.TillingActions
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.logic.*
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack
import kotlin.math.min


// this code is a modified version fabric-hammers mod
// https://github.com/bdani0717/fabric-hammers-1.20
fun getShapeAndPos(client: MinecraftClient): Pair<VoxelShape, BlockPos>? {
    val player = client.player ?: return null
    val world = client.world ?: return null

    if (player.isSneaking) return null

    val stack = player.mainHandStack
    val comp = getKosmogliphsOnStack(stack)
    if (comp.isEmpty()) return null

    val glyph = comp.outShapeChanger() ?: return null


    if (client.crosshairTarget is BlockHitResult) {
        val pos: BlockPos = (client.crosshairTarget as BlockHitResult).blockPos
        val state = client.world!!.getBlockState(pos)

        if (!state.isAir && client.world!!.worldBorder.contains(pos)) {
            val positions = glyph.getBlocks(stack, world, pos, state, player)
            if (positions.isEmpty()) return null

            var outlineShape = VoxelShapes.empty()
            for (position in positions) {
                val diffPos = position.subtract(pos)
                val offsetBlock = world.getBlockState(position)

                if (!offsetBlock.isAir) {
                    outlineShape = VoxelShapes.union(
                        outlineShape, offsetBlock.getOutlineShape(world, diffPos, ShapeContext.of(player))
                            .offset(diffPos.x.toDouble(), diffPos.y.toDouble(), diffPos.z.toDouble())
                    )
                }
            }
            return outlineShape to pos
        }
    }
    return null
}


fun KosmogliphsComponent.outShapeChanger(): SimpleKosmogliph? = if (this.contains(HAMMER)) HAMMER
else if (this.contains(REAPER)) REAPER
else if (this.contains(VEIN_MINER)) VEIN_MINER
else null

fun Kosmogliph.getBlocks(
    stack: ItemStack, world: World, pos: BlockPos, state: BlockState, player: PlayerEntity
): Set<BlockPos> {
    return when (this) {
        HAMMER -> queryMineableHammerPositions(stack, world, pos, state, player)
        REAPER -> queryReaperOutline(stack, world, pos, state, player)
        VEIN_MINER -> queryMineableVeinPositions(
            stack, world, state, pos, 30.0, min(64, stack.maxDamage - stack.damage)
        )

        else -> emptySet()
    }
}

fun queryReaperOutline(
    stack: ItemStack, world: World, pos: BlockPos, state: BlockState, player: PlayerEntity
): Set<BlockPos> {
    val mineable = queryReaperMineablePositions(stack, world, pos, state)
    if (mineable.isNotEmpty()) return mineable
    return queryReaperTillable(pos, world, player)
}

fun queryReaperTillable(pos: BlockPos, world: World, miner: PlayerEntity): Set<BlockPos> {
    val castEnd = miner.eyePos.add(miner.rotationVector.multiply(miner.reach()))
    val raycast = world.raycast(
        RaycastContext(
            miner.eyePos,
            castEnd,
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            miner
        )
    )

    return areaOfAffect(pos, raycast.side).allInside()
        .filter {
            if (!it.isInWorld(world)) return@filter false
            val predicate = TillingActions.get[world.getBlockState(it).block] ?: return@filter false
            predicate.first.test(ItemUsageContext(miner, miner.activeHand, raycast))
        }.toSet()

}
