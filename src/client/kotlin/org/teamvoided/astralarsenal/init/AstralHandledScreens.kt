package org.teamvoided.astralarsenal.init

import net.minecraft.client.gui.screen.ingame.HandledScreens
import org.teamvoided.astralarsenal.screens.CosmicTableScreen

object AstralHandledScreens {
    fun init() {
        HandledScreens.register(AstralMenus.COSMIC_TABLE, ::CosmicTableScreen)
    }
}