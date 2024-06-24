package org.teamvoided.astral_arsenal

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object AstralArsenal {
    const val MODID = "astral_arsenal"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(AstralArsenal::class.simpleName)

    fun init() {
        ModRegistry.init()
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
