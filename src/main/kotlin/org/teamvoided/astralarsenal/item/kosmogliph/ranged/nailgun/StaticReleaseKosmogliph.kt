package org.teamvoided.astralarsenal.item.kosmogliph.ranged.nailgun

import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class StaticReleaseKosmogliph (id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_OVER_HEAT) }) {
}