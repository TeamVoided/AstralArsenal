package org.teamvoided.astralarsenal.data.tags

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralBlockTags {
    val VEIN_MINEABLE = create("vein_mineable")
    val DUMMY = create("dummy")

    private fun create(id: String): TagKey<Block> = RegistryKeys.BLOCK.tag(id(id))
}