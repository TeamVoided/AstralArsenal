package org.teamvoided.astral_arsenal.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import org.teamvoided.astral_arsenal.AstralArsenal
import org.teamvoided.astral_arsenal.CosmicTableData
import org.teamvoided.astral_arsenal.CosmicTableScreenHandler

object AsScreenHandlers {

    val COSMIC_TABLE_SCREEN_HANDLER_TYPE =
        registryScreenHandlerType(
            "cosmic_table",
            ExtendedScreenHandlerType({ syncId, playerInventory, _ ->
                CosmicTableScreenHandler(syncId, playerInventory)
            }, CosmicTableData.PACKET_CODEC)
        )

    fun init() {}

    private fun <T : ScreenHandlerType<*>> registryScreenHandlerType(name: String, handler: T): T {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), handler)
    }
}