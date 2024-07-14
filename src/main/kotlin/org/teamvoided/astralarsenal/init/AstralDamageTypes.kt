package org.teamvoided.astralarsenal.init

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.astralarsenal.AstralArsenal.id


object AstralDamageTypes {
    val CANNONBALL: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("cannonball"))

    fun Entity.customDamage(type:RegistryKey<DamageType>, amount: Float) {
        this.damage(this.damageSources.create(type), amount)
    }
}