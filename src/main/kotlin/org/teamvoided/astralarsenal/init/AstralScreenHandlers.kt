package org.teamvoided.astralarsenal.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import org.teamvoided.astralarsenal.AstralArsenal

object AstralScreenHandlers {

    private fun <T : ScreenHandlerType<*>> registryScreenHandlerType(name: String, handler: T): T {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, AstralArsenal.id(name), handler)
    }
}