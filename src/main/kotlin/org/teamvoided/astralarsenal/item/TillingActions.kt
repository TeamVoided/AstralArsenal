package org.teamvoided.astralarsenal.item

import com.mojang.datafixers.util.Pair
import net.minecraft.block.Block
import net.minecraft.item.HoeItem
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ToolMaterials
import java.util.function.Consumer
import java.util.function.Predicate

abstract class TillingActions : HoeItem(ToolMaterials.WOOD, Settings()) {
    companion object {
        val get get():MutableMap<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> = TILLING_ACTIONS
    }
}