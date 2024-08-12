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
    override fun onHit(attacker: Entity?, victim: LivingEntity) {
        victim.addStatusEffect(StatusEffectInstance(AstralEffects.REDUCE, 200), attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE, Enchantments.CHANNELING)
    }

    override fun translationText(tooltip: Boolean) =
        "Reduce"
}