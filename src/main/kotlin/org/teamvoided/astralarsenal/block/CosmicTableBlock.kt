package org.teamvoided.astralarsenal.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.astralarsenal.block.entity.CosmicTableBlockEntity
import org.teamvoided.astralarsenal.init.AstralBlocks


class CosmicTableBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState.with(WATERLOGGED, false)
    }

    override fun getCodec(): MapCodec<CosmicTableBlock> = CODEC
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = CosmicTableBlockEntity(pos, state)
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL
    override fun getOutlineShape(s: BlockState, w: BlockView, p: BlockPos, c: ShapeContext): VoxelShape = SHAPE
    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, entity: PlayerEntity, hitResult: BlockHitResult
    ): ActionResult {
        return if (world.isClient()) ActionResult.SUCCESS
        else {
            entity.openHandledScreen(world.getBlockEntity(pos, AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY).orElseThrow())
            ActionResult.CONSUME
        }
    }

    override fun onStateReplaced(
        state: BlockState, world: World, pos: BlockPos, newState: BlockState, movedByPiston: Boolean
    ) {
        ItemScatterer.scatterInventory(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, movedByPiston)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        return super.getPlacementState(ctx)?.with(WATERLOGGED, fluidState.isOf(Fluids.WATER))
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED) as Boolean) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(true)
        else super.getFluidState(state)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>)
            : BlockEntityTicker<T>? =
        if (world.isClient) checkType(type, AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, CosmicTableBlockEntity::tick)
        else null

    companion object {
        val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)
        val CODEC = createCodec(::CosmicTableBlock)
        val WATERLOGGED = Properties.WATERLOGGED
    }
}