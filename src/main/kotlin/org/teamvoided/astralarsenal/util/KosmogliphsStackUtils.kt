package org.teamvoided.astralarsenal.util

import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.DEFAULT
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.toComponent
import org.teamvoided.astralarsenal.init.AstralDataComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph

fun ItemStack.getKosmogliphs(): KosmogliphsComponent = this.getOrDefault(KOSMOGLIPHS, DEFAULT)
fun ItemStack.setKosmogliphs(vararg kosmogliphs: Kosmogliph): ItemStack {
    this.getKosmogliphs().forEach { it.onUnapply(this) }
    this.set(KOSMOGLIPHS, kosmogliphs.toSet().toComponent())
    this.getKosmogliphs().forEach { it.onApply(this) }
    return this
}

fun ItemStack.hasKosmogliphs() = this.getKosmogliphs().isNotEmpty()
fun ItemStack.hasKosmogliph(kosmogliph: Kosmogliph) = this.getKosmogliphs().contains(kosmogliph)

