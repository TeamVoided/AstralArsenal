package org.teamvoided.astralarsenal.item.kosmogliph.armor

import net.minecraft.item.ArmorItem
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class BoostKosmogliph (id: Identifier) : SimpleKosmogliph(id, {
    val item = it.item
    item is ArmorItem && item.armorSlot == ArmorItem.ArmorSlot.BOOTS
}) {
    //Something needs to be done to just give this a jump boost, that is all but idk where to start
}