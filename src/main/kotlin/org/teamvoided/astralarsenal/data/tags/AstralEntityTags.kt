package org.teamvoided.astralarsenal.data.tags

import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralEntityTags {
    val MOUNTS_WITH_DASH = create("mounts_with_dash")

    private fun create(id: String): TagKey<EntityType<*>> = RegistryKeys.ENTITY_TYPE.tag(id(id))
}