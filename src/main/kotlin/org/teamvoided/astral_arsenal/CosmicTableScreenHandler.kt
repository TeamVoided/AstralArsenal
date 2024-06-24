package org.teamvoided.astral_arsenal

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketEncoder
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.Slot
import org.teamvoided.astral_arsenal.ModRegistry.COSMIC_TABLE_SCREEN_HANDLER_TYPE

class CosmicTableScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val context: ScreenHandlerContext
) :
    ScreenHandler(COSMIC_TABLE_SCREEN_HANDLER_TYPE, syncId) {

    private val inventory = object: SimpleInventory(2) {
        override fun markDirty() {
            super.markDirty()
            onContentChanged(this)
        }
    }

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(
        syncId,
        playerInventory,
        ScreenHandlerContext.EMPTY
    )

    init {
        addSlot(Slot(inventory, 0, 50, 50))
        addSlot(Slot(inventory, 1, 68, 50))

        for (y in 0..2) {
            for (x in 0..8) {
                addSlot(Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18))
            }
        }

        for (x in 0..9) {
            addSlot(Slot(playerInventory, x, 8 + x * 18, 142))
        }
    }

    override fun onContentChanged(inventory: Inventory?) {
        super.onContentChanged(inventory)
    }

    override fun quickTransfer(player: PlayerEntity?, fromIndex: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return canUse(context, player, ModRegistry.COSMIC_TABLE)
    }
}

class CosmicTableData {

    companion object {
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, CosmicTableData> =
            PacketCodec.create(PacketEncoder { _: RegistryByteBuf, _: CosmicTableData -> }) { CosmicTableData() }
    }
}