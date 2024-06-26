package org.teamvoided.astral_arsenal

import net.minecraft.client.gui.screen.ingame.HandledScreens
import org.teamvoided.astral_arsenal.init.AsScreenHandlers

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        HandledScreens.register(AsScreenHandlers.COSMIC_TABLE_SCREEN_HANDLER_TYPE, ::CosmicTableScreen)
    }
}
