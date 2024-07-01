package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.astralarsenal.command.KosmogliphCommand
import org.teamvoided.astralarsenal.init.*

@Suppress("unused")
object AstralArsenal {
    const val MOD_ID = "astral_arsenal"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(AstralArsenal::class.simpleName)

    fun init() {
        //Referencing object will initialize them
        AstralBlocks
        AstralItems
        AstralItemComponents
        AstralScreenHandlers
        AstralTabs
        AstralKosmogliphs

        CommandRegistrationCallback.EVENT.register { dispatcher, ctx, env ->
            val root = dispatcher.register(literal("astral"))
            KosmogliphCommand.apply(root, ctx, env)
        }
    }

    fun id(path: String): Identifier =
        Identifier.of(MOD_ID, path)
}
