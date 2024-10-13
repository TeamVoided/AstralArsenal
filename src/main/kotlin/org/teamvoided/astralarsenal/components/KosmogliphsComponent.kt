package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.TooltipAppender
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import java.util.function.Consumer

class KosmogliphsComponent(
    private val kosmogliphs: Set<Kosmogliph> = mutableSetOf()
) : TooltipAppender, Set<Kosmogliph> by kosmogliphs {
    companion object {
        val CODEC: Codec<KosmogliphsComponent> =
            Identifier.CODEC.xmap(Companion::fromId, Companion::toId).listOf().xmap(
                Companion::fromList,
                Companion::toList
            )

        fun toList(component: KosmogliphsComponent) = component.kosmogliphs.toList()
        fun fromList(list: List<Kosmogliph>) = KosmogliphsComponent(list.toSet())

        fun toId(kosmogliph: Kosmogliph) = Kosmogliph.REGISTRY.getId(kosmogliph)!!
        fun fromId(id: Identifier): Kosmogliph = Kosmogliph.REGISTRY.get(id)!!

        fun Collection<Kosmogliph>.toComponent() = KosmogliphsComponent(this.toSet())
    }

    override fun appendToTooltip(context: Item.TooltipContext, tooltipConsumer: Consumer<Text>, config: TooltipConfig) =
        kosmogliphs.forEach { tooltipConsumer.accept(Text.translatable(it.translationKey(true)).setColor(0x915eb4)) }
}