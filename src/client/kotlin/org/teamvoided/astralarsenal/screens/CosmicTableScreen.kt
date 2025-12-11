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
import org.teamvoided.astralarsenal.util.kosmogliphsCapable

class CosmicTableScreen(
    handler: CosmicTableMenu, inventory: PlayerInventory, title: Text
) : HandledScreen<CosmicTableMenu>(handler, inventory, title) {
    private val currentWidgets = mutableListOf<KosmogliphWidget>()
    private var lastTickStack = handler.getSlot(0).stack
    private var errorText = false


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

        if (ItemStack.itemsAndComponentsMatch(lastTickStack, applicationSlot.stack)) {
            if (!applicationSlot.hasStack()) {
                currentWidgets.forEach(::remove)
                currentWidgets.clear()
            }
            return
        }

        if (!(applicationSlot.hasStack() && (gemSlot.hasStack() || applicationSlot.stack.kosmogliphsCapable() || handler.playerInventory.player.isCreative) && (lastTickStack == applicationSlot.stack))) {
            currentWidgets.forEach(::remove)
            currentWidgets.clear()
        }
        if (currentWidgets.isEmpty()) {
            createWidgets()
            currentWidgets.forEach { addDrawableSelectableElement(it) }
        }

        lastTickStack = handler.getSlot(0).stack
    }

    private fun createWidgets() {
        val applicableKosmogliphs = Kosmogliph.REGISTRY.holders().toList().map { it.value() }
            .filter { it.canBeAppliedTo(handler.getSlot(0).stack) }

        val positions = getPositions(applicableKosmogliphs.size)
        errorText = positions.isEmpty() && applicableKosmogliphs.isNotEmpty()

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
//                if (client?.player?.isCreative == true || !handler.getSlot(1).stack.isEmpty) {
//                    handler.getSlot(0).stack.setKosmogliphs(kosmogliph)
//                }
            }
        }

        currentWidgets.addAll(widgets)
    }

    private fun getPositions(count: Int): List<Vector2i> {
        if (count <= 0) return emptyList()
        val halfSize = SIZE / 2
        val y = y + 14 + GAP
        val x = (this.width / 2) - halfSize

        val y2 = y + GAP + SIZE

        return when (count) {
            1 -> listOf(Vector2i(x, y))
            2 -> listOf(Vector2i(x - 30, y), Vector2i(x + 30, y))
            3 -> listOf(Vector2i(x - 30, y), Vector2i(x, y), Vector2i(x + 30, y))
            4 -> listOf(
                Vector2i(x - 60, y), Vector2i(x - 20, y),
                Vector2i(x + 20, y), Vector2i(x + 60, y)
            )

            5 -> makeTopRow(x, y)

            6 -> listOf(
                Vector2i(x - 60, y),
                Vector2i(x - 20, y),
                Vector2i(x + 20, y),
                Vector2i(x + 60, y)
            )
                .plus(Vector2i(x - 40, y2))
                .plus(Vector2i(x + 40, y2))

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

    override fun drawBackground(graphics: GuiGraphics, delta: Float, mouseX: Int, mouseY: Int) {
        val x = (this.width - this.backgroundWidth) / 2
        val y = (this.height - this.backgroundHeight) / 2
        graphics.fillRenderLayer(RenderLayer.getEndPortal(), x + 5, y + 5, x + (WIDTH - 5), y + (HEIGHT / 2), 0)
        graphics.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.drawForeground(graphics, mouseX, mouseY)
        if (errorText) graphics.drawCenteredShadowedText(
            this.textRenderer,
            Text.translatable("kosmogliph.cosmic_table.too_many_applicable"),
            this.backgroundWidth / 2,
            this.titleY + 23,
            0xFF0000
        )
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(graphics, mouseX, mouseY, delta)
        drawMouseoverTooltip(graphics, mouseX, mouseY)
    }

    companion object {
        val TEXTURE = AstralArsenal.id("textures/gui/container/cosmic_table.png")
        const val WIDTH = 176
        const val HEIGHT = 165

        const val GAP = 4
    }
}