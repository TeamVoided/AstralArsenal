package org.teamvoided.astralarsenal.data.tags

import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralDamageTypeTags {
    val IS_MAGIC = create("is_magic")
    val IS_FIRE = create("is_fire")

    private fun create(id: String): TagKey<DamageType> = RegistryKeys.DAMAGE_TYPE.tag(id(id))
}