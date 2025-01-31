package org.teamvoided.astralarsenal.components

import com.mojang.serialization.Codec
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.TooltipAppender
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import java.util.function.Consumer

class KosmogliphsComponent(private val kosmogliphs: Set<Kosmogliph> = mutableSetOf()) : TooltipAppender,
    Set<Kosmogliph> by kosmogliphs {
    fun has(kosmogliph: Kosmogliph) = kosmogliphs.contains(kosmogliph)
    override fun appendToTooltip(context: Item.TooltipContext, tooltipConsumer: Consumer<Text>, config: TooltipConfig) =
        kosmogliphs.forEach { tooltipConsumer.accept(Text.translatable(it.translationKey(true)).setColor(0x915eb4)) }


    companion object {
        val DEFAULT = KosmogliphsComponent()
        val CODEC: Codec<KosmogliphsComponent> = Identifier.CODEC
            .xmap({ Kosmogliph.REGISTRY.get(it)!! }, { Kosmogliph.REGISTRY.getId(it)!! })
            .listOf().xmap({ it.toComponent() }, { it.kosmogliphs.toList() })

        fun Collection<Kosmogliph>.toComponent() = KosmogliphsComponent(this.toSet())
    }
}