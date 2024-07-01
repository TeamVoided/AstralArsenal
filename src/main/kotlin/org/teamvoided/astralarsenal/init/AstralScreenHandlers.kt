package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.CosmicTableData
import org.teamvoided.astralarsenal.CosmicTableScreenHandler

object AstralScreenHandlers {
    val COSMIC_TABLE_SCREEN_HANDLER_TYPE =
        registryScreenHandlerType(
            "cosmic_table",
            ExtendedScreenHandlerType({ syncId, playerInventory, _ ->
                CosmicTableScreenHandler(syncId, playerInventory)
            }, CosmicTableData.PACKET_CODEC)
        )

    private fun <T : ScreenHandlerType<*>> registryScreenHandlerType(name: String, handler: T): T {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), handler)
    }
}