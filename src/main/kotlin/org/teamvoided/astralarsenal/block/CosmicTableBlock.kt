package org.teamvoided.astralarsenal.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.FurnaceBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.teamvoided.astralarsenal.block.enity.CosmicTableBlockEntity
import org.teamvoided.astralarsenal.init.AstralBlocks


class CosmicTableBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<CosmicTableBlock> = createCodec(::CosmicTableBlock)

    override fun createBlockEntity(
        pos: BlockPos,
        state: BlockState
    ): BlockEntity {
        return CosmicTableBlockEntity(pos, state)
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getOutlineShape(
        state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape = SHAPE

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient()) return ActionResult.SUCCESS_NO_ITEM_USED

        entity.openHandledScreen(world.getBlockEntity(pos, AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY).orElseThrow())

        return ActionResult.SUCCESS_NO_ITEM_USED
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (!state.isOf(newState.block)) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is CosmicTableBlockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity)
            }
            super.onStateReplaced(state, world, pos, newState, movedByPiston)
        }
    }

    companion object {
        val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)
    }
}