package org.teamvoided.astralarsenal.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Model
import net.minecraft.data.client.model.Models
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.init.AstralItems
import java.util.*

class AstralModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {
    }

    override fun generateItemModels(gen: ItemModelGenerator) {
        val excludelist = listOf(AstralItems.ASTRAL_GREATHAMMER, AstralItems.RAILGUN)
        AstralItems.items().filter{!excludelist.contains(it)}.forEach {
            if (it is BlockItem) return@forEach
            gen.register(it, Models.SINGLE_LAYER_ITEM)
        }
    }

    fun ItemModelGenerator.registerBlockParented(item: BlockItem) {
        this.register(item, item.parentModel)
    }

    val BlockItem.parentModel
        get() = Model(Optional.of(Registries.BLOCK.getId(this.block).withPrefix("block/")), Optional.empty())
}