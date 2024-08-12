package org.teamvoided.astralarsenal.item.kosmogliph.tools

import net.minecraft.item.AxeItem
import net.minecraft.item.HoeItem
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ShovelItem
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.SimpleKosmogliph

class HammerKosmogliph(id: Identifier) : SimpleKosmogliph(id,
    { it.item is PickaxeItem || it.item is ShovelItem || it.item is AxeItem || it.item is HoeItem })