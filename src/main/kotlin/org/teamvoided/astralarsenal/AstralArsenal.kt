package org.teamvoided.astralarsenal

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.init.AstralItems
import org.teamvoided.astralarsenal.init.AstralScreenHandlers
import org.teamvoided.astralarsenal.init.AstralTabs

@Suppress("unused")
object AstralArsenal {
    const val MOD_ID = "astral_arsenal"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(AstralArsenal::class.simpleName)

    fun init() {
        //Referencing object will initialize them
        AstralBlocks
        AstralItems
        AstralScreenHandlers
        AstralTabs
    }

    fun id(path: String): Identifier =
        Identifier.of(MOD_ID, path)
}
