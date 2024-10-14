package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.init.AstralEffects

class TridentBleedKosmogliph(id: Identifier) : ThrownTridentKosmogliph(id, AstralItemTags.SUPPORTS_TRIDENT_BLEED) {
    val over = listOf(
        AstralEffects.BLEED
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
                AstralEffects.BLEED,
                400, bleed_levels,
                false, false, true
            )
        )
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        var bleed_levels = 0
        val effects_two = target.statusEffects.filter { over.contains(it.effectType) }
        if (effects_two.isNotEmpty()) {
            effects_two.forEach {
                val w = it.amplifier
                bleed_levels += w + 1
            }
        }
        target.addStatusEffect(
            StatusEffectInstance(
                AstralEffects.BLEED,
                400, bleed_levels,
                false, false, true
            )
        )
        super.postHit(stack, target, attacker)
    }

    override fun translationText(tooltip: Boolean) =
        "Bleed"


    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}