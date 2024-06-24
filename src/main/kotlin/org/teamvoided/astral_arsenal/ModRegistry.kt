package org.teamvoided.astral_arsenal

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.BlockSoundGroup
import org.teamvoided.astral_arsenal.blocks.CosmicTableBlock

object ModRegistry {

    val COSMIC_TABLE: Block =
        registerBlock("cosmic_table", CosmicTableBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(
            BlockSoundGroup.LODESTONE)))

    val COSMIC_TABLE_SCREEN_HANDLER_TYPE =
        registryScreenHandlerType(
            "cosmic_table",
            ExtendedScreenHandlerType({ syncId, playerInventory, _ ->
                CosmicTableScreenHandler(syncId, playerInventory)
            }, CosmicTableData.PACKET_CODEC)
        )

    fun init() {}

    private fun registerBlock(name: String, block: Block): Block {
        val instance = Registry.register(Registries.BLOCK, name, block)
        Registry.register(Registries.ITEM, AstralArsenal.id(name), BlockItem(instance, Item.Settings()))
        return instance
    }

    private fun <T : ScreenHandlerType<*>> registryScreenHandlerType(name: String, handler: T): T {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), handler)
    }
}