package org.teamvoided.astralarsenal.kosmogliph.ranged.cometlauncher

import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.kosmogliph.SimpleKosmogliph

class QuickshotKosmogliph(id: Identifier) : SimpleKosmogliph(id, { it.isIn(AstralItemTags.SUPPORTS_QUICKSHOT) }) {
}