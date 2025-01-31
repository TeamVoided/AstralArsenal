package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.components.AstralStrikeData
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.init.AstralDataComponents
import org.teamvoided.astralarsenal.kosmogliph.KosmogliphWithData

// I will fix this - Astra
class AstralStrikeKosmogliph(id: Identifier) :
    KosmogliphWithData(id, AstralDataComponents.ASTRAL_STRIKE_DATA, AstralItemTags.SUPPORTS_ASTRAL_STRIKE) {

    val STRIKES_TO_TRIGGER = 8

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val data = stack.getOrDefault(AstralDataComponents.ASTRAL_STRIKE_DATA, AstralStrikeData.DEFAULT)
        var hitTimes = data.hitTimes
        hitTimes++
        if (hitTimes >= STRIKES_TO_TRIGGER) {
            val beam = BeamOfLightEntity(attacker.world, attacker)
            beam.setPosition(target.pos)
            beam.WINDUP = 20
            beam.TIMEACTIVE = 10
            beam.side = 2
            beam.THRUST = 1.0
            beam.targetEntity = target
            beam.DOT = false
            beam.DMG = 5
            beam.trackTime = 15
            beam.hard_damage = 0
            attacker.world.spawnEntity(beam)
            hitTimes = 0
        }
        stack.set(AstralDataComponents.ASTRAL_STRIKE_DATA, AstralStrikeData(hitTimes))
        return super.postHit(stack, target, attacker)
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.FIRE_ASPECT)
    }
}