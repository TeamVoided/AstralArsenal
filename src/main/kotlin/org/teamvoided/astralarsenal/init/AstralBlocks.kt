package org.teamvoided.astralarsenal.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.blocks.CosmicTableBlock

object AstralBlocks {
    val COSMIC_TABLE: Block =
        registerBlock(
            "cosmic_table", CosmicTableBlock(
                AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(
                    BlockSoundGroup.LODESTONE
                )
            )
        )

    private fun registerBlock(name: String, block: Block): Block {
        val instance = Registry.register(Registries.BLOCK, AstralArsenal.id(name), block)
        Registry.register(Registries.ITEM, AstralArsenal.id(name), BlockItem(instance, Item.Settings()))
        return instance
    }
}