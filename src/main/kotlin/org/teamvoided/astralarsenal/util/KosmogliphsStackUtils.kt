package org.teamvoided.astralarsenal.util

import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.DEFAULT
import org.teamvoided.astralarsenal.components.KosmogliphsComponent.Companion.toComponent
import org.teamvoided.astralarsenal.init.AstralDataComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.init.AstralKosmogliphs
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph

fun ItemStack.getKosmogliphs(): KosmogliphsComponent = getOrDefault(KOSMOGLIPHS, DEFAULT)
fun ItemStack.setKosmogliphs(vararg kosmogliphs: Kosmogliph): ItemStack {
    getKosmogliphs().forEach { it.onUnapply(this) }
    set(KOSMOGLIPHS, kosmogliphs.toSet().toComponent())
    getKosmogliphs().forEach { it.onApply(this) }
    return this
}

fun ItemStack.removeKosmogliphs(): ItemStack {
    getKosmogliphs().forEach { it.onUnapply(this) }
    remove(KOSMOGLIPHS)
    return this
}

fun ItemStack.setEmpty(): ItemStack {
    getKosmogliphs().forEach { it.onUnapply(this) }
    set(KOSMOGLIPHS, KosmogliphsComponent(AstralKosmogliphs.EMPTY))
    getKosmogliphs().forEach { it.onApply(this) }
    return this
}

fun ItemStack.hasKosmogliph(kosmogliph: Kosmogliph) = getKosmogliphs().contains(kosmogliph)
fun ItemStack.hasKosmogliphs(): Boolean = getKosmogliphs().isNotEmpty() && !hasKosmogliph(AstralKosmogliphs.EMPTY)
fun ItemStack.kosmogliphsCapable(): Boolean = getKosmogliphs().isNotEmpty()

