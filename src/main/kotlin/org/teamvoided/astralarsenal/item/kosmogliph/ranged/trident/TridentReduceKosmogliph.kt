package org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralEffects

class TridentReduceKosmogliph(id: Identifier) : ThrownTridentKosmogliph(id) {
    override fun onHit(attacker: Entity?, victim: LivingEntity) {
        victim.addStatusEffect(StatusEffectInstance(AstralEffects.REDUCE, 600), attacker)
    }

    override fun translationText(tooltip: Boolean) =
        "Reduce"
}