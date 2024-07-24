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
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralMenus
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

class CosmicTableMenu(
    syncId: Int,
    val playerInventory: PlayerInventory,
    val data: CosmicTableData = CosmicTableData(SimpleInventory(2)),
) : ScreenHandler(AstralMenus.COSMIC_TABLE, syncId) {
    init {
        addSlot(object : Slot(data.inventory, 0, 70, 55) {
            override fun canInsert(stack: ItemStack) = kosmogliphCanBeApplied(stack)
        })
        addSlot(object : Slot(data.inventory, 1, 90, 55) {
            override fun canInsert(stack: ItemStack) = stack.isOf(AstralItems.KOSMIC_GEM)
        })

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
        val kosmicGemSlot = getSlot(1)
        if (!kosmicGemSlot.hasStack() || !kosmicGemSlot.stack.isOf(AstralItems.KOSMIC_GEM)) return false

        val stack = getSlot(0).stack
        val kosmicGemStack = kosmicGemSlot.stack
        val applicable = applicableKosmogliphs()
        val kosmogliph = applicable[id]

        stack.set(AstralItemComponents.KOSMOGLIPHS, KosmogliphsComponent(setOf(kosmogliph)))
        kosmicGemStack.count--

        return true
    }

    fun applicableKosmogliphs(): List<Kosmogliph> {
        return Kosmogliph.REGISTRY.holders()
            .toList()
            .map { it.value() }
            .filter { it.canBeAppliedTo(getSlot(0).stack) }
    }

    fun kosmogliphCanBeApplied(stack: ItemStack) = stack.get(AstralItemComponents.KOSMOGLIPHS) != null
    fun isKosmikGem(stack: ItemStack) = stack.isOf(AstralItems.KOSMIC_GEM)

    override fun quickTransfer(
        player: PlayerEntity,
        fromIndex: Int
    ): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot = slots[fromIndex]
        if (!slot.hasStack()) return newStack

        val originalStack = slot.stack
        newStack = originalStack.copy()
        if (fromIndex < data.inventory.size()) {
            if (!this.insertItem(originalStack, data.inventory.size(), slots.size, true)) {
                return ItemStack.EMPTY
            }
        } else if (!this.insertItem(originalStack, 0, data.inventory.size(), false)) {
            return ItemStack.EMPTY
        }

        if (originalStack.isEmpty) {
            slot.stack = ItemStack.EMPTY
        } else {
            slot.markDirty()
        }

        return newStack
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
