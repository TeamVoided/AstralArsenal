package org.teamvoided.astralarsenal.item.kosmogliph

import arrow.core.Predicate
import net.minecraft.entity.damage.DamageType
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class DamageReductionKosmogliph(id: Identifier, val multiplier: Float, applicationPredicate: Predicate<ItemStack>, vararg val tags: TagKey<DamageType>):
    SimpleKosmogliph(id, applicationPredicate) {

}