package org.teamvoided.astralarsenal

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EquipmentSlot
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.astralarsenal.command.KosmogliphCommand
import org.teamvoided.astralarsenal.init.*
import org.teamvoided.astralarsenal.networking.JumpKosmogliphPayload

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
        AstralMenus
        AstralTabs
        AstralKosmogliphs
        AstralDamageTypes
        AstralSounds
        AstralEntities.serverInit()

        CommandRegistrationCallback.EVENT.register { dispatcher, ctx, env ->
            val root = dispatcher.register(literal("astral"))
            KosmogliphCommand.apply(root, ctx, env)
        }

        PayloadTypeRegistry.playC2S().register(JumpKosmogliphPayload.ID, JumpKosmogliphPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(JumpKosmogliphPayload.ID) { _, ctx ->
            val player = ctx.player()
            val stack = player.getEquippedStack(EquipmentSlot.FEET)
            val kosmogliphs = stack.get(AstralItemComponents.KOSMOGLIPHS) ?: setOf()
            if (!kosmogliphs.contains(AstralKosmogliphs.JUMP)) return@registerGlobalReceiver
            AstralKosmogliphs.JUMP.handleJump(stack, player)
        }
    }

    fun id(path: String): Identifier =
        Identifier.of(MOD_ID, path)
}
