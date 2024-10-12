package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageScalingType
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.astralarsenal.data.registry.RegistryBootstrapper

object AstralDamageTypes : RegistryBootstrapper<DamageType>(RegistryKeys.DAMAGE_TYPE) {
    val CANNONBALL = register("cannonball") { DamageType("cannonball", DamageScalingType.NEVER, 0f) }
    val BALLNT = register("ballnt") { DamageType("ballnt", DamageScalingType.NEVER, 0f) }
    val BEAM_OF_LIGHT = register("beam_of_light") { DamageType("beam_of_light", DamageScalingType.NEVER, 0f) }
    val RAILED = register("railed") { DamageType("railed", DamageScalingType.NEVER, 0f) }
    val NON_RAILED = register("non_railed") { DamageType("non_railed", DamageScalingType.NEVER, 0f) }
    val BLEED = register("bleed") { DamageType("bleed", DamageScalingType.NEVER, 0f) }
    val DRAIN = register("drain") { DamageType("drain", DamageScalingType.NEVER, 0f) }
    val BURN = register("burn") { DamageType("burn", DamageScalingType.NEVER, 0f) }
    val BOOM = register("boom") { DamageType("boom", DamageScalingType.NEVER, 0f) }
    val PARRY = register("parry") { DamageType("parry", DamageScalingType.NEVER, 0f) }
    val RICHOCHET = register("richochet") { DamageType("richochet", DamageScalingType.NEVER, 0f) }
    val NAILED = register("nailed") { DamageType("nailed", DamageScalingType.NEVER, 0f) }

    fun Entity.customDamage(
        type: RegistryKey<DamageType>,
        amount: Float,
        source: Entity? = null,
        attacker: Entity? = null
    ): Boolean {
        return this.damage(this.damageSources.create(type, source, attacker), amount)
    }
}