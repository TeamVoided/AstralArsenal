package org.teamvoided.astralarsenal.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.IdentifierArgumentType.getIdentifier
import net.minecraft.command.argument.IdentifierArgumentType.identifier
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import org.teamvoided.astralarsenal.kosmogliph.Kosmogliph
import org.teamvoided.astralarsenal.util.error
import org.teamvoided.astralarsenal.util.message
import org.teamvoided.astralarsenal.util.setKosmogliphs

object KosmogliphCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val kosmogliphNode = literal("kosmogliph").requires { it.hasPermission(2) }.build()

        val arg = argument("kosmogliph", identifier()).suggests { _, builder ->
            Kosmogliph.REGISTRY.keys.map { it.value.toString() }
                .filter { it.lowercase().contains(builder.remainingLowerCase) }
                .forEach(builder::suggest)
            builder.buildFuture()
        }.executes(::cmd).build()
        kosmogliphNode.addChild(arg)

        dispatcher.root.addChild(kosmogliphNode)
    }

    fun cmd(ctx: CommandContext<ServerCommandSource>): Int {
        val kosmogliph = Kosmogliph.REGISTRY.get(getIdentifier(ctx, "kosmogliph")) ?: return -1
        val src = ctx.source
        val player = src.player ?: return -1
        val stack = player.mainHandStack ?: return -1
        if (stack.isEmpty) return src.error("Cant apply kosmogliph to empty hand!")
        if (!kosmogliph.canBeAppliedTo(stack)) return src.error("Cant apply kosmogliph to item ${stack.item}!")

        stack.setKosmogliphs(kosmogliph)
        src.message("Applied kosmogliph ${kosmogliph.id()} !")
        return Command.SINGLE_SUCCESS
    }
}