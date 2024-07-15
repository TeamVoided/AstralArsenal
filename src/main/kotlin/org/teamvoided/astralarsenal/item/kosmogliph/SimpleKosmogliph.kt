package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Predicate
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

open class SimpleKosmogliph(
    val id: Identifier,
    val applicationPredicate: Predicate<ItemStack>
) : Kosmogliph {
    override fun modifyItemTooltip(
        stack: ItemStack,
        ctx: Item.TooltipContext,
        tooltip: MutableList<Text>,
        config: TooltipConfig
    ) {
        tooltip += Text.translatable(id.toTranslationKey("kosmogliph.tooltip"))
    }

    override fun canBeAppliedTo(stack: ItemStack): Boolean {
        return applicationPredicate(stack)
    }
}