package org.teamvoided.astralarsenal.init

import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object AstralEffects {
    fun init() {}
    val SLAM_JUMP = register(
        "slam_jump",
        StatusEffect(StatusEffectType.BENEFICIAL, 6684672).addAttributeModifier(
            EntityAttributes.GENERIC_JUMP_STRENGTH,
            ,
            1,
            EntityAttributeModifier.Operation.ADD_VALUE
        )
    );
    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> {
        return Registry.registerHolder(Registries.STATUS_EFFECT, sun.tools.jstat.Identifier(id), entry)
    }
}