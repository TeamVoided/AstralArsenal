package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

abstract class ThrownTridentKosmogliph(id: Identifier, tag: TagKey<Item>) : SimpleKosmogliph(id, { it.isIn(tag) }) {
    abstract fun onHit(attacker: Entity?, victim: LivingEntity)
}