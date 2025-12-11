package org.teamvoided.astralarsenal.data.gen.prov

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator.createSingletonBlockState
import net.minecraft.data.client.model.Models
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralBlocks.COSMIC_TABLE
import org.teamvoided.astralarsenal.init.AstralItems

class AstralModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {
        val model = COSMIC_TABLE.blockModel()
        gen.registerParentedItemModel(COSMIC_TABLE, model)
        gen.blockStateCollector.accept(createSingletonBlockState(COSMIC_TABLE, model))
    }

    val hasModel = listOf(
        AstralItems.ASTRAL_GREATHAMMER,
        AstralItems.RAILGUN,
        COSMIC_TABLE.asItem(),
        AstralItems.NAILCANNON,
        AstralItems.TOME_OF_HEXES
    )

    override fun generateItemModels(gen: ItemModelGenerator) {
        AstralItems.items().filterNot(hasModel::contains).forEach { gen.register(it, Models.SINGLE_LAYER_ITEM) }
    }

    private fun Block.blockModel(): Identifier = Registries.BLOCK.getId(this).withPrefix("block/")
}