package org.teamvoided.astralarsenal.screens.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.screen.narration.NarrationPart
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

abstract class KosmogliphWidget(
    x: Int, y: Int,
    width: Int, height: Int,
    message: Text,
    val kosmogliph: Kosmogliph
) : ClickableWidget(x, y, width, height, message) {
    var active = false
    val kosmogliphTexture by lazy {
        kosmogliph.id().withPrefix("textures/gui/kosmogliph/")
    }

    override fun drawWidget(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        graphics.drawTexture(if (active) ACTIVE_TEXTURE else INACTIVE_TEXTURE, x, y, 0, 0, width, height)
        // TODO Get textures and uncomment line
        //graphics.drawTexture(kosmogliphTexture, x, y, 0, 0, width, height)
    }

    override fun updateNarration(builder: NarrationMessageBuilder) {
        builder.put(NarrationPart.HINT, Text.translatable(kosmogliph.id().toTranslationKey("kosmogliph.name")))
    }

    companion object {
        val INACTIVE_TEXTURE = AstralArsenal.id("textures/gui/widget/kosmogliph_inactive.png")
        val ACTIVE_TEXTURE = AstralArsenal.id("textures/gui/widget/kosmogliph_active.png")
    }
}

fun KosmogliphWidget(
    x: Int, y: Int,
    width: Int, height: Int,
    message: Text,
    kosmogliph: Kosmogliph,
    onWidgetClick: KosmogliphWidget.(x: Double, y: Double) -> Unit
): KosmogliphWidget = object : KosmogliphWidget(x, y, width, height, message, kosmogliph) {
    override fun onClick(mouseX: Double, mouseY: Double) {
        onWidgetClick(mouseX, mouseY)
    }
}
