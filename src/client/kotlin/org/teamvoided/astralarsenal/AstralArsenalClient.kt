package org.teamvoided.astralarsenal

import net.minecraft.client.gui.screen.ingame.HandledScreens
import org.teamvoided.astralarsenal.init.AstralScreenHandlers

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        HandledScreens.register(AstralScreenHandlers.COSMIC_TABLE_SCREEN_HANDLER_TYPE, ::CosmicTableScreen)
    }
}
