package org.teamvoided.astralarsenal.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandler
import org.teamvoided.astralarsenal.AstralArsenal
import org.teamvoided.astralarsenal.menu.CosmicTableData
import org.teamvoided.astralarsenal.menu.CosmicTableMenu

object AstralMenus {
    val COSMIC_TABLE = register<CosmicTableMenu>("cosmic_table", ::CosmicTableMenu)

    fun <T : ScreenHandler> register(name: String, factory: ExtendedScreenHandlerType.ExtendedFactory<T, CosmicTableData>): ExtendedScreenHandlerType<T, CosmicTableData> {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), ExtendedScreenHandlerType(factory, CosmicTableData.PACKET_CODEC))
    }
}