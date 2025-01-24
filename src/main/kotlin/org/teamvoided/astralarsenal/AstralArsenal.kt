package org.teamvoided.astralarsenal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.astralarsenal.command.KosmogliphCommand
import org.teamvoided.astralarsenal.coroutine.mcCoroutineDispatcher
import org.teamvoided.astralarsenal.coroutine.mcCoroutineScope
import org.teamvoided.astralarsenal.init.*

@Suppress("unused")
object AstralArsenal {
    const val MOD_ID = "astral_arsenal"
    const val DEFAULT_KEY_CATEGORY = "key.category.${MOD_ID}.key_bindings"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(AstralArsenal::class.simpleName)

    fun init() {
        //Referencing object will initialize them
        AstralBlocks.init()
        AstralItems
        AstralDataComponents.init()
        AstralScreenHandlers
        AstralMenus.init()
        AstralTabs
        AstralKosmogliphs
        AstralDamageTypes
        AstralSounds
        AstralEffects.init()
        AstralEntities.init()
        AstralNetworking.init()
        AstralParticles.init()

        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            mcCoroutineDispatcher = server.asCoroutineDispatcher()
            mcCoroutineScope = CoroutineScope(SupervisorJob() + mcCoroutineDispatcher)
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, ctx, env ->
            @Suppress("UNUSED_VARIABLE") val root = dispatcher.register(literal("astral"))
            KosmogliphCommand.register(dispatcher)
        }
    }

    fun id(path: String): Identifier =
        Identifier.of(MOD_ID, path)
}
