package org.teamvoided.astralarsenal.kosmogliph.ranged.trident

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags

class TensionKosmogliph(id: Identifier) : ThrownTridentKosmogliph(id, AstralItemTags.SUPPORTS_TRIDENT_BLEED) {
    override fun onHit(attacker: Entity?, victim: LivingEntity) {
        if (attacker != null && attacker is LivingEntity) {
            val moveVec = attacker.eyePos.subtract(victim.eyePos)
            victim.velocity = moveVec.multiply(0.4, 0.1, 0.4)
                .multiply(1 - victim.attributes.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
            victim.velocityModified = true
        }
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val moveVec = attacker.eyePos.subtract(target.eyePos)
        target.velocity = moveVec.multiply(0.3, 0.1, 0.3)
            .multiply(1 - target.attributes.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
        target.velocityModified = true
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.LOYALTY)
    }
}