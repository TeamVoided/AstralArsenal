package org.teamvoided.astralarsenal.data.tags

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.tag

object AstralItemTags {

    // name these better idc I made them so they exists
    val WEARABLE_BOOTS = create("wearable/boots")
    val WEARABLE_LEGGINGS = create("wearable/leggings")
    val WEARABLE_CHESTPLATES = create("wearable/chestplate")
    val WEARABLE_HELMETS = create("wearable/helmet")

    private fun create(id: String): TagKey<Item> = RegistryKeys.ITEM.tag(id(id))
}