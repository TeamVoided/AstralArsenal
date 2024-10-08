package org.teamvoided.astralarsenal.init

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.effects.AstralStatusEffect
import org.teamvoided.astralarsenal.effects.BleedStatusEffect
import org.teamvoided.astralarsenal.util.registerHolder

object AstralEffects {
    fun init() = Unit
    val SLAM_JUMP = register(
        "slam_jump", AstralStatusEffect(StatusEffectType.BENEFICIAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_JUMP_STRENGTH, id("effect.jump"),
                0.1, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val UNHEALABLE_DAMAGE = register(
        "unhealable_damage", AstralStatusEffect(StatusEffectType.NEUTRAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_HEALTH, id("effect.unhealable"),
                -0.5, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val HARD_DAMAGE = register(
        "hard_damage", AstralStatusEffect(StatusEffectType.NEUTRAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_HEALTH, id("effect.weak_hard"),
                -0.05, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val OVERHEAL = register(
        "overheal", AstralStatusEffect(StatusEffectType.BENEFICIAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MAX_ABSORPTION, id("effect.overheal"),
                0.2, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )
    val REDUCE = register(
        "reduce", AstralStatusEffect(StatusEffectType.HARMFUL, 0x660000)
    )
    val BLEED = register(
        "bleed", BleedStatusEffect(0x660000)
    )

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)

    val REDUCE_MULT = 0.05
    val reduce = listOf(
        REDUCE
    )
    fun modifyDamage(entity: LivingEntity, damage: Float): Float {
        var output = damage
        val effects_two = entity.statusEffects.filter { reduce.contains(it.effectType) }
        if (effects_two.isNotEmpty()) {
            effects_two.forEach {
                val w = it.amplifier
                val levels = w + 1
                val mult = levels * REDUCE_MULT
                output = (output * (1 + mult)).toFloat()
            }
        }
        return output
    }
}