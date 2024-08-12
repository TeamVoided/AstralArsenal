package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.data.tags.AstralItemTags
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class HammerKosmogliph(id: Identifier) : SimpleKosmogliph(id,
    { it.isIn(AstralItemTags.SUPPORTS_HAMMER) })