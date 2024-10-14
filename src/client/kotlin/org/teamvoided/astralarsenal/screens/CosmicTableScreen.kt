package org.teamvoided.astralarsenal.screens

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import org.joml.Vector2i
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.menu.CosmicTableMenu
import org.teamvoided.astralarsenal.screens.widget.KosmogliphWidget
import org.teamvoided.astralarsenal.screens.widget.KosmogliphWidget.Companion.SIZE

class CosmicTableScreen(
    handler: CosmicTableMenu, inventory: PlayerInventory, title: Text
) : HandledScreen<CosmicTableMenu>(handler, inventory, title) {
    private val currentWidgets = mutableListOf<KosmogliphWidget>()
    private var lastTickStack = ItemStack.EMPTY


    init {
        titleY -= 2
        playerInventoryTitleY += 1
    }
    // opening with is 160px

    override fun init() {
        super.init()
        currentWidgets.forEach(::remove)
        currentWidgets.clear()
        createWidgets()
        currentWidgets.forEach(::addDrawableSelectableElement)
    }

    override fun handledScreenTick() {
        val applicationSlot = handler.getSlot(0)
        val gemSlot = handler.getSlot(1)

        if (!(applicationSlot.hasStack() && (gemSlot.hasStack() || handler.hasKosmogliph(applicationSlot.stack) || handler.playerInventory.player.isCreative) && (lastTickStack == applicationSlot.stack))) {
            currentWidgets.forEach(::remove)
            currentWidgets.clear()
        } else if (currentWidgets.isEmpty()) {
            createWidgets()
            currentWidgets.forEach { addDrawableSelectableElement(it) }
        }

        lastTickStack = handler.getSlot(0).stack
    }

    private fun createWidgets() {
        val applicableKosmogliphs = Kosmogliph.REGISTRY.holders().toList().map { it.value() }
            .filter { it.canBeAppliedTo(handler.getSlot(0).stack) }

        val positions = getPositions(applicableKosmogliphs.size)
        //determineWidgetPositions(applicableKosmogliphs.size, Vector2i(x + 8, y + 14), 2)

        val widgets = positions.mapIndexed { index, position ->
            KosmogliphWidget(
                position.x,
                position.y,
                SIZE,
                SIZE,
                Text.empty(),
                applicableKosmogliphs[index],
                handler
            ) { x, y ->
                client!!.interactionManager!!.clickButton(handler.syncId, index)
                Kosmogliph.addToComponent(handler.getSlot(0).stack, kosmogliph)
            }
        }

        currentWidgets.addAll(widgets)
    }

    private fun getPositions(count: Int): List<Vector2i> {

        val halfSize = SIZE / 2
        val y = y + 14 + GAP
        val x = (this.width / 2) - halfSize

        val y2 = y + GAP + SIZE

        return when (count) {
            0 -> emptyList()
            1 -> listOf(Vector2i(x, y))
            2 -> listOf(Vector2i(x - 30, y), Vector2i(x + 30, y))
            3 -> listOf(Vector2i(x - 30, y), Vector2i(x, y), Vector2i(x + 30, y))
            4 -> listOf(
                Vector2i(x - 60, y),
                Vector2i(x - 20, y),
                Vector2i(x + 20, y),
                Vector2i(x + 60, y)
            )

            5 -> makeTopRow(x, y)

            6 -> listOf(
                Vector2i(x - 60, y),
                Vector2i(x - 20, y),
                Vector2i(x + 20, y),
                Vector2i(x + 60, y)
            ).plus(Vector2i(x - 40, y2)).plus(Vector2i(x + 40, y2))

            7 -> makeTopRow(x, y)
                .plus(Vector2i(x - 50, y2))
                .plus(Vector2i(x + 50, y2))

            else -> emptyList() //throw IllegalStateException("Are more than 10 kosmogliphs are applicable to this item?")
        }
    }

    fun makeTopRow(x: Int, y: Int) = listOf(
        Vector2i(x - 60, y),
        Vector2i(x - 30, y),
        Vector2i(x, y),
        Vector2i(x + 30, y),
        Vector2i(x + 60, y)
    )

    private fun determineWidgetPositions(count: Int, offset: Vector2i, gap: Int): List<Vector2i> {
        if (count <= 0) return emptyList()

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
        ucPositions[underCount - 1]()

        return positions
    }

    override fun drawBackground(
        graphics: GuiGraphics, delta: Float, mouseX: Int, mouseY: Int
    ) {
        val x = (this.width - this.backgroundWidth) / 2
        val y = (this.height - this.backgroundHeight) / 2
        graphics.fillRenderLayer(RenderLayer.getEndPortal(), x + 5, y + 5, x + (WIDTH - 5), y + (HEIGHT / 2), 0)
        graphics.drawTexture(TEXTURE, x, y, 0, 0, WIDTH, HEIGHT)
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(graphics, mouseX, mouseY, delta)
        drawMouseoverTooltip(graphics, mouseX, mouseY)
    }

    companion object {
        val TEXTURE = AstralArsenal.id("textures/gui/container/cosmic_table.png")
        const val WIDTH = 175
        const val HEIGHT = 165

        const val GAP = 4
    }
}