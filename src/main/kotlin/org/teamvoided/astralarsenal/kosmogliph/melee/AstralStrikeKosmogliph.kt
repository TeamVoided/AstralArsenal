package org.teamvoided.astralarsenal.kosmogliph.melee

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.entity.BeamOfLightEntity
import org.teamvoided.astralarsenal.init.AstralItemComponents
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

// I will fix this - Astra
class AstralStrikeKosmogliph(id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_ASTRAL_STRIKE) }) {

        val STRIKES_TO_TRIGGER = 8

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val data = stack.get(AstralItemComponents.ASTRAL_STRIKE_DATA)
            ?: throw IllegalStateException("Erm, how the fuck did you manage this")
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
        stack.set(AstralItemComponents.ASTRAL_STRIKE_DATA, Data(hitTimes))
        return super.postHit(stack, target, attacker)
    }

    data class Data(
        val hitTimes: Int,
    ) {
        companion object {
            val CODEC = Codecs.NONNEGATIVE_INT.listOf()
                .xmap(
                    { list -> Data(list[0]) },
                    { data -> listOf(data.hitTimes) }
                )
        }
    }

    override fun disallowedEnchantment(): List<RegistryKey<Enchantment>> {
        return listOf(Enchantments.FIRE_ASPECT)
    }

    override fun requiredEnchantments(): List<RegistryKey<Enchantment>> {
        return listOf()
    }
}