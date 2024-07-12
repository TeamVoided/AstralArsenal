package org.teamvoided.astralarsenal

import org.teamvoided.astralarsenal.init.AstralEntitiesClient
import org.teamvoided.astralarsenal.init.AstralHandledScreens

@Suppress("unused")
object AstralArsenalClient {

    fun init() {
        AstralHandledScreens
        AstralEntitiesClient.clientInit()
    }
}
