package org.teamvoided.astralarsenal.kosmogliph.ranged

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.components.AlchemistData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph
import kotlin.jvm.optionals.getOrNull

class UnphasedKosmogliph(id: Identifier) : SimpleKosmogliph(id, AstralItemTags.SUPPORTS_SMELTER), BowKosmogliph{

    override fun overrideArrowType(player: PlayerEntity, stack: ItemStack, original: ItemStack): ItemStack? {
        if (!player.isCreative && original.isEmpty) return null

        if (!player.isCreative) original.decrement(1)

        val data = stack.getOrDefault(AstralDataComponents.ALCHEMIST_DATA, AlchemistData.DEFAULT)
        if (data.charges <= 0 || data.contents.isEmpty) return null
        val potion = data.contents.getOrNull() ?: return null
        val tippedArrow = ItemStack(Items.TIPPED_ARROW)
        tippedArrow.set(DataComponentTypes.POTION_CONTENTS, potion)



        return tippedArrow
    }
}