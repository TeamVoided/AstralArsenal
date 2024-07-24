package org.teamvoided.astralarsenal.item.kosmogliph.ranged.trident

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralEffects

class TridentBleedKosmogliph(id: Identifier) : ThrownTridentKosmogliph(id) {
    override fun onHit(attacker: Entity?, victim: LivingEntity) {
        victim.addStatusEffect(StatusEffectInstance(AstralEffects.BLEED, 200), attacker)
    }

    override fun translationText(tooltip: Boolean) =
        "Bleed"

    override fun onApply(stack: ItemStack) {
        super.onApply(stack)

        val builder = ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(stack))
        builder.removeIf { enchantment ->
            enchantment.isRegistryKey(Enchantments.RIPTIDE)
        }
        stack.set(
            DataComponentTypes.ENCHANTMENTS,
            builder.build()
        )
    }
}