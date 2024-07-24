package org.teamvoided.astralarsenal.util

import arrow.core.Predicate
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.init.AstralItemComponents.KOSMOGLIPHS
import org.teamvoided.astralarsenal.item.components.KosmogliphsComponent
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.item.kosmogliph.ranged.BowKosmogliph

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun <T> Registry<T>.registerHolder(id: Identifier, entry:T): Holder.Reference<T> = Registry.registerHolder(this, id, entry)
fun <T> Registry<T>.register(id: Identifier, entry:T): T = Registry.register(this, id, entry)

fun getKosmogliphsOnStack(stack: ItemStack): KosmogliphsComponent {
    val component = stack.components.get(KOSMOGLIPHS)
        ?: return KosmogliphsComponent()
    return component
}

fun interface BPredicate<T>: Predicate<T>, java.util.function.Predicate<T> {
    override fun test(t: T): Boolean = this(t)
}

fun Iterable<Kosmogliph>.findFirstBow(): BowKosmogliph? {
    forEach { kosmogliph ->
        if (kosmogliph is BowKosmogliph) return kosmogliph
    }

    return null
}
