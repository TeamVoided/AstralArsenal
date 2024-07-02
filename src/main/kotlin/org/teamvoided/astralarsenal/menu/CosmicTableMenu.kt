package org.teamvoided.astralarsenal.menu

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import org.teamvoided.astralarsenal.init.AstralMenus
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

class CosmicTableMenu(
    syncId: Int,
    val playerInventory: PlayerInventory,
    val data: CosmicTableData = CosmicTableData(SimpleInventory(2)),
) : ScreenHandler(AstralMenus.COSMIC_TABLE, syncId) {
    init {
        addSlot(Slot(data.inventory, 0, 70, 55))
        addSlot(Slot(data.inventory, 1, 90, 55))

        for (i in 0..<3) {
            for (j in 0..<9) {
                this.addSlot(Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
            }
        }

        for (i in 0..<9) {
            this.addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }

    override fun onButtonClick(player: PlayerEntity, id: Int): Boolean {
        val applicable = applicableKosmogliphs()
        val kosmogliph = applicable[id]
        val result = kosmogliph.apply(getSlot(0).stack)

        return result.isRight()
    }

    fun applicableKosmogliphs(): List<Kosmogliph> {
        return Kosmogliph.REGISTRY.holders()
            .toList()
            .map { it.value() }
            .filter { it.canBeAppliedTo(getSlot(0).stack) }
    }

    override fun quickTransfer(
        player: PlayerEntity,
        fromIndex: Int
    ): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity): Boolean = true
}

data class CosmicTableData(
    val inventory: Inventory = SimpleInventory(2),
) {
    companion object {
        val PACKET_CODEC: PacketCodec<PacketByteBuf, CosmicTableData> =
            PacketCodec.create(::encode, ::decode)

        fun encode(buf: PacketByteBuf, data: CosmicTableData) {}

        fun decode(buf: PacketByteBuf): CosmicTableData {
            return CosmicTableData()
        }
    }
}
