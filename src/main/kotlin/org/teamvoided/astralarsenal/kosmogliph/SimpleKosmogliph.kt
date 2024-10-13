package org.teamvoided.astralarsenal.kosmogliph

import arrow.core.Predicate
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

open class SimpleKosmogliph(val id: Identifier, val applicationPredicate: Predicate<ItemStack>) : Kosmogliph {
    constructor(id: Identifier, tag: TagKey<Item>) : this(id, { it.isIn(tag) })

    override fun canBeAppliedTo(stack: ItemStack): Boolean {
        return applicationPredicate(stack)
    }
}