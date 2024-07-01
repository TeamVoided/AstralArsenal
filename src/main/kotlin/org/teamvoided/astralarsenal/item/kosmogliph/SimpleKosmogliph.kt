package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Either
import arrow.core.Predicate
import arrow.core.left
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class SimpleKosmogliph(
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

    override fun apply(stack: ItemStack): Either<Kosmogliph.ApplicationFailure, ItemStack> {
        if (!applicationPredicate(stack)) return Kosmogliph.ApplicationFailure(Text.translatable("kosmogliph.error.inapplicable")).left()
        return super.apply(stack)
    }
}