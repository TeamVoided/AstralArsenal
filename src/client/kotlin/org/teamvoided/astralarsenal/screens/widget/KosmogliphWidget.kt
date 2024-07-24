package org.teamvoided.astralarsenal.screens.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.screen.narration.NarrationPart
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

abstract class KosmogliphWidget(
    x: Int, y: Int,
    width: Int, height: Int,
    message: Text,
    val kosmogliph: Kosmogliph
) : ClickableWidget(x, y, width, height, message) {
    var active = false
    val kosmogliphTexture = kosmogliph.id().withPrefix("kosmogliph/")


    init {
        tooltip = Tooltip.create(Text.translatable(kosmogliph.translationKey(true)))
    }

    override fun drawWidget(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        val stack = graphics.matrices
        stack.push()
        graphics.drawGuiTexture(if (active) ACTIVE_TEXTURE else INACTIVE_TEXTURE, x, y, width, height)
        graphics.drawGuiTexture(kosmogliphTexture, x + 2, y + 2, (width / 1.2).toInt(), (height / 1.2).toInt())
        stack.pop()
    }

    override fun updateNarration(builder: NarrationMessageBuilder) {
        builder.put(NarrationPart.HINT, Text.translatable(kosmogliph.id().toTranslationKey("kosmogliph.name")))
    }

    companion object {
        val INACTIVE_TEXTURE = id("widget/kosmogliph/inactive")
        val ACTIVE_TEXTURE = id("widget/kosmogliph/active")

        const val SIZE = 22
    }
}

fun KosmogliphWidget(
    x: Int, y: Int, width: Int, height: Int, message: Text, kosmogliph: Kosmogliph,
    onWidgetClick: KosmogliphWidget.(x: Double, y: Double) -> Unit
): KosmogliphWidget = object : KosmogliphWidget(x, y, width, height, message, kosmogliph) {
    override fun onClick(mouseX: Double, mouseY: Double) {
        onWidgetClick(mouseX, mouseY)
    }
}
