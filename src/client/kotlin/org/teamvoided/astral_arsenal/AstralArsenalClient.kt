package org.teamvoided.astral_arsenal

import net.minecraft.client.gui.screen.ingame.HandledScreens

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        HandledScreens.register(ModRegistry.COSMIC_TABLE_SCREEN_HANDLER_TYPE, ::CosmicTableScreen)
    }
}
