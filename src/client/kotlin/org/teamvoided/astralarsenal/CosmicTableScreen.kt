package org.teamvoided.astralarsenal

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class CosmicTableScreen(handler: CosmicTableScreenHandler, inventory: PlayerInventory, title: Text) :
    HandledScreen<CosmicTableScreenHandler>(handler, inventory, title) {

    companion object {
        val TEXTURE: Identifier = AstralArsenal.id("textures/gui/container/cosmic_table.png")
    }

    override fun drawBackground(graphics: GuiGraphics, delta: Float, mouseX: Int, mouseY: Int) {
        graphics.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }
}
