package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Predicate
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier

open class SimpleKosmogliph(
    val id: Identifier,
    val applicationPredicate: Predicate<ItemStack>
) : Kosmogliph {
    constructor(id: Identifier, tag: TagKey<Item>) : this(id, { it.isIn(tag) })

    override fun modifyItemTooltip(
        stack: ItemStack,
        ctx: Item.TooltipContext,
        tooltip: MutableList<Text>,
        config: TooltipConfig
    ) {
        tooltip += Text.translatable(id.toTranslationKey("kosmogliph.tooltip")).setColor(0x54326b)
    }

    override fun canBeAppliedTo(stack: ItemStack): Boolean {
        return applicationPredicate(stack)
    }
}