package org.teamvoided.astralarsenal.block.enity

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.menu.CosmicTableData
import org.teamvoided.astralarsenal.menu.CosmicTableMenu

class CosmicTableBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : LootableContainerBlockEntity(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, pos, state), ExtendedScreenHandlerFactory<CosmicTableData> {
    private val inventory = DefaultedList.ofSize(2, ItemStack.EMPTY)

    override fun getContainerName(): Text = Text.translatable("container.cosmic_table")

    override fun method_11282(): DefaultedList<ItemStack> = inventory

    override fun method_11281(defaultedList: DefaultedList<ItemStack>) {
        inventory.clear()
        inventory.addAll(defaultedList)
    }

    override fun createScreenHandler(
        syncId: Int,
        playerInventory: PlayerInventory
    ): ScreenHandler {
        return CosmicTableMenu(syncId, playerInventory, CosmicTableData(this))
    }

    override fun size(): Int = inventory.size

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        Inventories.writeNbt(nbt, inventory, lookupProvider)
    }

    override fun method_11014(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.method_11014(nbt, lookupProvider)
        Inventories.readNbt(nbt, inventory, lookupProvider)
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity): CosmicTableData {
        return CosmicTableData(this)
    }
}