package org.teamvoided.astralarsenal.screens.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.screen.narration.NarrationPart
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.menu.CosmicTableMenu
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

abstract class KosmogliphWidget(
    x: Int, y: Int,
    width: Int, height: Int,
    message: Text,
    val kosmogliph: Kosmogliph,
    val handler: CosmicTableMenu
) : ClickableWidget(x, y, width, height, message) {
    val kosmogliphTexture: Identifier = kosmogliph.id().withPrefix("kosmogliph/")
    val BTN_TEXTURE: Identifier

    init {
        tooltip = Tooltip.create(handler.createKosmogliphTooltip(kosmogliph))
        BTN_TEXTURE = getTexture()
    }

    override fun drawWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        graphics.matrices.push()
        graphics.drawGuiTexture(BTN_TEXTURE, x, y, width, height)
        graphics.drawGuiTexture(kosmogliphTexture, x + 2, y + 2, (width / 1.2).toInt(), (height / 1.2).toInt())
        graphics.matrices.pop()
    }

    override fun updateNarration(builder: NarrationMessageBuilder) {
        builder.put(NarrationPart.HINT, Text.translatable(kosmogliph.id().toTranslationKey("kosmogliph.name")))
    }

    private fun getTexture(): Identifier {
        return if (handler.isIncompatible(kosmogliph, handler.getSlot(0).stack)) INCOMPATIBLE_TEXTURE
        else if (getKosmogliphsOnStack(handler.getSlot(0).stack).contains(kosmogliph)) SELECTED_TEXTURE
        else COMPATIBLE_TEXTURE
    }

    companion object {
        val INCOMPATIBLE_TEXTURE = id("widget/kosmogliph/incompatible")
        val SELECTED_TEXTURE = id("widget/kosmogliph/selected")
        val COMPATIBLE_TEXTURE = id("widget/kosmogliph/compatible")

        const val SIZE = 22
    }
}

fun KosmogliphWidget(
    x: Int, y: Int, width: Int, height: Int, message: Text, kosmogliph: Kosmogliph, handler: CosmicTableMenu,
    onWidgetClick: KosmogliphWidget.(x: Double, y: Double) -> Unit
): KosmogliphWidget = object : KosmogliphWidget(x, y, width, height, message, kosmogliph, handler) {
    override fun onClick(mouseX: Double, mouseY: Double) {
        onWidgetClick(mouseX, mouseY)
    }
}
