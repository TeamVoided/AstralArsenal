package org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.TridentItem
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

abstract class ThrownTridentKosmogliph(
    id: Identifier,
) : SimpleKosmogliph(id, { it.item is TridentItem }) {
    abstract fun onHit(attacker: Entity?, victim: LivingEntity)
}