package org.teamvoided.astral_arsenal

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import org.teamvoided.astral_arsenal.blocks.CosmicTableBlock

object ModRegistry {

    val COSMIC_TABLE: Block =
        registerBlock(
            "cosmic_table", CosmicTableBlock(
                AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(
                    BlockSoundGroup.LODESTONE
                )
            )
        )

    val COSMIC_TABLE_SCREEN_HANDLER_TYPE =
        registryScreenHandlerType(
            "cosmic_table",
            ExtendedScreenHandlerType({ syncId, playerInventory, _ ->
                CosmicTableScreenHandler(syncId, playerInventory)
            }, CosmicTableData.PACKET_CODEC)
        )

    val TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, AstralArsenal.id("astral_arsenal"))

    fun init() {
        Registry.register(
            Registries.ITEM_GROUP,
            TAB,
            FabricItemGroup.builder().name(Text.translatable("itemgroup.astral_arsenal.tab")).icon {
                ItemStack(COSMIC_TABLE.asItem())
            }.entries { _, entries -> entries.addItem(COSMIC_TABLE) }.build()
        )
    }

    private fun registerBlock(name: String, block: Block): Block {
        val instance = Registry.register(Registries.BLOCK, name, block)
        Registry.register(Registries.ITEM, AstralArsenal.id(name), BlockItem(instance, Item.Settings()))
        return instance
    }

    private fun <T : ScreenHandlerType<*>> registryScreenHandlerType(name: String, handler: T): T {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), handler)
    }
}