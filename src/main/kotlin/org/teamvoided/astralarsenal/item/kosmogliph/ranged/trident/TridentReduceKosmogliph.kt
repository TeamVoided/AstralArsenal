package org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralEffects

class TridentReduceKosmogliph(id: Identifier) :
    ThrownTridentKosmogliph(id, AstralItemTags.SUPPORTS_TRIDENT_REDUCE) {
    val over = listOf(
        AstralEffects.REDUCE
    )
    override fun onHit(attacker: Entity?, victim: LivingEntity) {
        var bleed_levels = 0
        val effects_two = victim.statusEffects.filter { over.contains(it.effectType) }
        if (effects_two.isNotEmpty()) {
            effects_two.forEach {
                val w = it.amplifier
                bleed_levels += w + 1
            }
        }
        victim.addStatusEffect(
            StatusEffectInstance(
                AstralEffects.REDUCE,
                400, bleed_levels,
                false, false, true
            )
        )
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

    override fun translationText(tooltip: Boolean) =
        "Reduce"
}