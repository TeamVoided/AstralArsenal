package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageScalingType
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.astralarsenal.data.registry.RegistryBootstrapper

object AstralDamageTypes : RegistryBootstrapper<DamageType>(RegistryKeys.DAMAGE_TYPE) {
    val CANNONBALL = register("cannonball") { DamageType("cannonball", DamageScalingType.ALWAYS, 0f) }
    val BEAM_OF_LIGHT = register("beam_of_light") { DamageType("beam_of_light", DamageScalingType.ALWAYS, 0f) }
    val BLEED = register("bleed") { DamageType("bleed", DamageScalingType.NEVER, 0f) }

    fun Entity.customDamage(type:RegistryKey<DamageType>, amount: Float): Boolean {
        return this.damage(this.damageSources.create(type), amount)
    }
}