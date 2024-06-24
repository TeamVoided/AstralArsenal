package org.teamvoided.astral_arsenal.blocks

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.astral_arsenal.CosmicTableData
import org.teamvoided.astral_arsenal.CosmicTableScreenHandler

class CosmicTableBlock(settings: Settings) : Block(settings) {

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient()) {
            return ActionResult.SUCCESS
        }

        entity.openHandledScreen(object : ExtendedScreenHandlerFactory<CosmicTableData> {
            override fun createMenu(
                syncId: Int,
                playerInventory: PlayerInventory,
                playerEntity: PlayerEntity
            ): ScreenHandler {
                return CosmicTableScreenHandler(syncId, playerInventory)
            }

            override fun getDisplayName(): Text {
                return Text.translatable("block.astral_arsenal.cosmic_table")
            }

            override fun getScreenOpeningData(player: ServerPlayerEntity?): CosmicTableData {
                return CosmicTableData()
            }
        })
        return ActionResult.CONSUME
    }
}