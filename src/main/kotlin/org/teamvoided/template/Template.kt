package org.teamvoided.template

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Template {
    const val MODID = "template"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Template::class.simpleName)

    fun init() {
        log.info("Hello from Common")
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
