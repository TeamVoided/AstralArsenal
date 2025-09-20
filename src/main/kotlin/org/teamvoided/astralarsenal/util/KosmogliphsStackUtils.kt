package org.teamvoided.astralarsenal.util

import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.DEFAULT
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.toComponent
import org.teamvoided.astralarsenal.init.AstralDataComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph

fun ItemStack.getKosmogliphs(): KosmogliphsComponent = this.getOrDefault(KOSMOGLIPHS, DEFAULT)
fun ItemStack.setKosmogliphs(vararg kosmogliphs: Kosmogliph): ItemStack {
    this.getKosmogliphs().forEach { it.onUnapply(this) }
    this.set(KOSMOGLIPHS, kosmogliphs.toSet().toComponent())
    this.getKosmogliphs().forEach { it.onApply(this) }
    return this
}

fun ItemStack.removeKosmogliphs(): ItemStack {
    this.getKosmogliphs().forEach { it.onUnapply(this) }
    this.remove(KOSMOGLIPHS)
    return this
}

fun ItemStack.setEmpty(): ItemStack {
    this.getKosmogliphs().forEach { it.onUnapply(this) }
    this.set(KOSMOGLIPHS, KosmogliphsComponent(AstralKosmogliphs.EMPTY))
    this.getKosmogliphs().forEach { it.onApply(this) }
    return this
}

fun ItemStack.hasKosmogliph(kosmogliph: Kosmogliph) = this.getKosmogliphs().contains(kosmogliph)
fun ItemStack.hasKosmogliphs(): Boolean {
    val gliphs = this.getKosmogliphs()
    return gliphs.isNotEmpty() && gliphs.has(AstralKosmogliphs.EMPTY)
}

