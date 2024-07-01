package org.teamvoided.astralarsenal.item.components

import com.mojang.serialization.Codec
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

class KosmogliphsComponent(
    private val kosmogliphs: Set<Kosmogliph> = mutableSetOf()
) : Set<Kosmogliph> by kosmogliphs {
    companion object {
        val CODEC: Codec<KosmogliphsComponent> =
            Identifier.CODEC.xmap(::fromId, ::toId).listOf().xmap(::fromList, ::toList)

        fun toList(component: KosmogliphsComponent) = component.kosmogliphs.toList()
        fun fromList(list: List<Kosmogliph>) = KosmogliphsComponent(list.toSet())

        fun toId(kosmogliph: Kosmogliph) = Kosmogliph.REGISTRY.getId(kosmogliph)!!
        fun fromId(id: Identifier): Kosmogliph = Kosmogliph.REGISTRY.get(id)!!

        fun Collection<Kosmogliph>.toComponent() = KosmogliphsComponent(this.toSet())
    }
}