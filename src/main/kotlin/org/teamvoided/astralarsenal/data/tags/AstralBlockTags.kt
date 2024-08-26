package org.teamvoided.astralarsenal.data.tags

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralBlockTags {
    val VEIN_MINEABLE = create("vein_mineable")
    val REAPABLE_CROPS = create("reapable_crops")

    private fun create(id: String): TagKey<Block> = RegistryKeys.BLOCK.tag(id(id))
}