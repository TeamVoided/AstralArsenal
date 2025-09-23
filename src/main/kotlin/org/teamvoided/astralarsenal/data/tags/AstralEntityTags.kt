package org.teamvoided.astralarsenal.data.tags

import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralEntityTags {
    val MOUNTS_WITH_DASH = create("mounts_with_dash")
    val PROTECTED_FROM_DEL = create("protected_from_del")
    @Deprecated("Idk what it do")
    val UNAFFECTED_BY_LIGHT = create("unaffected_by_light")

    val REFLECTABLE_PROJECTILES = create("reflectable_projectiles")

    val WEAK_PARRYABLES = create("weak_parryables")
    val STRONG_PARRYABLES = create("strong_parryables")
    val VERY_STRONG_PARRYABLES = create("very_strong_parryables")

    private fun create(id: String): TagKey<EntityType<*>> = RegistryKeys.ENTITY_TYPE.tag(id(id))
}