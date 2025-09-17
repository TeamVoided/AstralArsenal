package org.teamvoided.astralarsenal.kosmogliph

import net.minecraft.component.DataComponentType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.util.Predicate

open class KosmogliphWithData(
    id: Identifier, val dataType: DataComponentType<*>, applicationPredicate: Predicate<ItemStack>,
) : SimpleKosmogliph(id, applicationPredicate) {
    constructor(id: Identifier, dataType: DataComponentType<*>, tag: TagKey<Item>)
            : this(id, dataType, { it.isIn(tag) })

    override fun onUnapply(stack: ItemStack) {
        super.onUnapply(stack)
        stack.remove(dataType)
    }
}