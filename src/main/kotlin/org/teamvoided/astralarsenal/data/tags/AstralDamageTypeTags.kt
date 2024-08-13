package org.teamvoided.astralarsenal.data.tags

import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralDamageTypeTags {
    val IS_MAGIC = create("is_magic")
    val IS_FIRE = create("is_fire")
    val IS_PROJECTILE = create("is_projectile")
    val IS_MELEE = create("is_melee")
    val IS_EXPLOSION = create("is_explosion")
    val IS_PLASMA = create("is_plasma")
    val IS_ICE = create("is_ice")
    val KEEPS_MOVEMENT = create("keeps_movement")

    private fun create(id: String): TagKey<DamageType> = RegistryKeys.DAMAGE_TYPE.tag(id(id))
}