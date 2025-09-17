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
            val pos1 = attacker.eyePos
            val pos2 = victim.eyePos
            val moveVec = pos1.subtract(pos2)
            victim.velocity = moveVec.multiply(0.3, 0.1, 0.3)
                .multiply(1 - victim.attributes.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
            victim.velocityModified
        }
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val pos1 = attacker.eyePos
        val pos2 = target.eyePos
        val moveVec = pos1.subtract(pos2)
        target.velocity = moveVec.multiply(0.3, 0.1, 0.3)
            .multiply(1 - target.attributes.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
        target.velocityModified
        super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.RIPTIDE)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.LOYALTY)
    }
}