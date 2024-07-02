package org.teamvoided.astralarsenal.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
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

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient()) return ActionResult.PASS

        entity.openHandledScreen(world.getBlockEntity(pos, AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY).orElseThrow())

        return ActionResult.SUCCESS
    }
}