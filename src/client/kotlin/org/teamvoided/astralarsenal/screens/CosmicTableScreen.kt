package org.teamvoided.astralarsenal.screens

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import org.joml.Vector2i
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.menu.CosmicTableMenu
import org.teamvoided.astralarsenal.screens.widget.KosmogliphWidget

class CosmicTableScreen(
    handler: CosmicTableMenu,
    inventory: PlayerInventory,
    title: Text
) : HandledScreen<CosmicTableMenu>(handler, inventory, title) {
    private val currentWidgets = mutableListOf<KosmogliphWidget>()

    override fun init() {
        super.init()
    }

    override fun handledScreenTick() {
        if (!handler.getSlot(0).hasStack()) {
            currentWidgets.forEach(::remove)
            currentWidgets.clear()
        } else if (currentWidgets.isEmpty()) {
            createWidgets()
            currentWidgets.forEach { addDrawableSelectableElement(it) }
        }
    }

    private fun createWidgets() {
        val applicableKosmogliphs =
            Kosmogliph.REGISTRY.holders()
                .toList()
                .map { it.value() }
                .filter { it.canBeAppliedTo(handler.getSlot(0).stack) }

        val x = (this.width - this.backgroundWidth) / 2
        val y = (this.height - this.backgroundHeight) / 2
        val positions = determineWidgetPositions(applicableKosmogliphs.size, Vector2i(x + 8, y + 14), 2)

        val widgets = positions.mapIndexed { index, position ->
            KosmogliphWidget(position.x, position.y, 22, 22, Text.empty(), applicableKosmogliphs[index]) { x, y ->
                client!!.interactionManager!!.clickButton(handler.syncId, index)
                kosmogliph.apply(handler.getSlot(0).stack)
            }
        }

        currentWidgets.addAll(widgets)
    }

    private fun determineWidgetPositions(count: Int, offset: Vector2i, gap: Int): List<Vector2i> {
        val xSpan = 160
        val ySpan = 28
        var count = count
        var underCount = 0

        fun perWidgetX() = xSpan / count

        while (perWidgetX() <= (22 + (gap * 2))) {
            count--
            underCount++
        }

        val perWidgetX = perWidgetX()

        if (underCount > 4) throw IllegalStateException("Are more than 10 kosmogliphs are applicable to this item?")

        val positions = mutableListOf<Vector2i>()
        for (i in 0..<count) {
            positions.add(offset.add(perWidgetX * i + gap, gap, Vector2i()))
        }

        if (underCount <= 0) return positions

        fun ucPos1() = positions.add(offset.add(perWidgetX + gap * 2, ySpan + gap * 2, Vector2i()))

        fun ucPos2() {
            ucPos1()
            positions.add(offset.add(perWidgetX * 2 + 48 + gap * 4, ySpan + gap * 2, Vector2i()))
        }

        fun ucPos3() {
            ucPos2()
            positions.add(offset.add(gap, ySpan + gap * 2, Vector2i()))
        }

        fun ucPos4() {
            ucPos3()
            positions.add(offset.add(perWidgetX * 3 + 48 + gap * 5, ySpan + gap * 2, Vector2i()))
        }

        val ucPositions = listOf(::ucPos1, ::ucPos2, ::ucPos3, ::ucPos4)
        ucPositions[underCount-1]()

        return positions
    }

    override fun drawBackground(
        graphics: GuiGraphics,
        delta: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        val x = (this.width - this.backgroundWidth) / 2
        val y = (this.height - this.backgroundHeight) / 2
        graphics.drawTexture(TEXTURE, x, y, 0, 0, WIDTH, HEIGHT)
    }

    companion object {
        val TEXTURE = AstralArsenal.id("textures/gui/container/cosmic_table.png")
        val WIDTH = 175
        val HEIGHT = 165

    }
}