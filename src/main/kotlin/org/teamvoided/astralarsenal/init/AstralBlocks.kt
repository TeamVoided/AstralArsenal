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
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.block.CosmicTableBlock
import org.teamvoided.astralarsenal.block.entity.CosmicTableBlockEntity

object AstralBlocks {
    fun init() = Unit
    val COSMIC_TABLE: Block = registerBlock(
        "cosmic_table",
        CosmicTableBlock(
            AbstractBlock.Settings
                .copy(Blocks.CRAFTING_TABLE)
                .sounds(BlockSoundGroup.LODESTONE)
                .nonOpaque()
                .strength(10f)
                .luminance { 4 }

        )
    )

    val COSMIC_TABLE_BLOCK_ENTITY = registerBlockEntity(
        "cosmic_table", BlockEntityType.Builder.create(::CosmicTableBlockEntity, COSMIC_TABLE).build()
    )

    fun registerBlock(name: String, block: Block): Block {
        val instance = Registry.register(Registries.BLOCK, id(name), block)
        AstralItems.register(name, BlockItem(instance, Item.Settings()))
        return instance
    }

    fun <T : BlockEntity> registerBlockEntity(name: String, blockEntityType: BlockEntityType<T>): BlockEntityType<T> =
        Registry.register(Registries.BLOCK_ENTITY_TYPE, id(name), blockEntityType)
}