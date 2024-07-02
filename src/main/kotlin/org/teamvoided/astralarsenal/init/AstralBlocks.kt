package org.teamvoided.astralarsenal.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.block.CosmicTableBlock
import org.teamvoided.astralarsenal.block.enity.CosmicTableBlockEntity

object AstralBlocks {
    val COSMIC_TABLE: Block = registerBlock(
        "cosmic_table",
        CosmicTableBlock(
            AbstractBlock.Settings
                .copy(Blocks.CRAFTING_TABLE)
                .sounds(BlockSoundGroup.LODESTONE)
        )
    )
    val COSMIC_TABLE_BLOCK_ENTITY = registerBlockEntity("cosmic_table", BlockEntityType.Builder.create(::CosmicTableBlockEntity, COSMIC_TABLE).build())

    private fun registerBlock(name: String, block: Block): Block {
        val instance = Registry.register(Registries.BLOCK, AstralArsenal.id(name), block)
        Registry.register(Registries.ITEM, AstralArsenal.id(name), BlockItem(instance, Item.Settings()))
        return instance
    }

    private fun <T : BlockEntity> registerBlockEntity(name: String, blockEntityType: BlockEntityType<T>): BlockEntityType<T> {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, AstralArsenal.id(name), blockEntityType)
    }
}