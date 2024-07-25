package org.teamvoided.astralarsenal.data.gen.prov

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.init.AstralItems

class AstralModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {

        gen.registerBlockParented(AstralBlocks.COSMIC_TABLE)
        gen.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(AstralBlocks.COSMIC_TABLE, AstralBlocks.COSMIC_TABLE.blockModel()))
    }

    override fun generateItemModels(gen: ItemModelGenerator) {
        val excludelist =
            listOf(AstralItems.ASTRAL_GREATHAMMER, AstralItems.RAILGUN, AstralBlocks.COSMIC_TABLE.asItem())
        AstralItems.items().filter { !excludelist.contains(it) && it !is BlockItem }
            .forEach { gen.register(it, Models.SINGLE_LAYER_ITEM) }
    }

    private fun BlockStateModelGenerator.registerBlockParented(block: Block) {
        this.registerParentedItemModel(block, block.blockModel())
    }

    private fun Block.blockModel(): Identifier =  Registries.BLOCK.getId(this).withPrefix("block/")

//    val Block.parentModel
//        get() = Model(Optional.of(Registries.BLOCK.getId(this).withPrefix("block/")), Optional.empty())
}