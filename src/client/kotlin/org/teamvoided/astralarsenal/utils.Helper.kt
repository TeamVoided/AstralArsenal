package org.teamvoided.astralarsenal

import net.minecraft.block.ShapeContext
import net.minecraft.client.MinecraftClient
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.HAMMER
import org.teamvoided.astralarsenal.init.AstralKosmogliphs.VEIN_MINER
import org.teamvoided.astralarsenal.item.kosmogliph.logic.queryMineableHammerPositions
import org.teamvoided.astralarsenal.item.kosmogliph.logic.queryMineableVeinPositions
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

    val isHammer = comp.contains(HAMMER)
    if (!comp.contains(VEIN_MINER) && !isHammer) return null


    if (client.crosshairTarget is BlockHitResult) {
        val pos: BlockPos = (client.crosshairTarget as BlockHitResult).blockPos
        val state = client.world!!.getBlockState(pos)

        if (!state.isAir && client.world!!.worldBorder.contains(pos)) {
            val positions = if (isHammer) queryMineableHammerPositions(stack, world, pos, state, player) else
                queryMineableVeinPositions(stack, world, state, pos, 30.0, min(64, stack.maxDamage - stack.damage))

            var outlineShape = VoxelShapes.empty()

            if (positions.isEmpty()) return null

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
