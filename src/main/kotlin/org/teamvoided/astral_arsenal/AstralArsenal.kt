package org.teamvoided.astral_arsenal

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.astral_arsenal.init.AsBlocks
import org.teamvoided.astral_arsenal.init.AsScreenHandlers
import org.teamvoided.astral_arsenal.init.AsTabs
import org.teamvoided.astral_arsenal.init.AstItems

@Suppress("unused")
object AstralArsenal {

    const val MODID = "astral_arsenal"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(AstralArsenal::class.simpleName)

    fun init() {
        AsBlocks.init()
        AstItems.init()
        AsScreenHandlers.init()
        AsTabs.init()
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
