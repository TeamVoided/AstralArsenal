package org.teamvoided.astralarsenal.item.kosmogliph.shield

import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class FrostThornsKosmogliph (id: Identifier) :
    SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_FROST_THORNS) }) {
}