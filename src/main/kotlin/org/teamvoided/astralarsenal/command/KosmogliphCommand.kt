package org.teamvoided.astralarsenal.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.command.CommandBuildContext
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import org.teamvoided.astralarsenal.item.kosmogliph.Kosmogliph

object KosmogliphCommand {
    fun apply(
        node: LiteralCommandNode<ServerCommandSource>,
        ctx: CommandBuildContext,
        env: RegistrationEnvironment
    ) {
        val cmdNode = literal("kosmogliph")
            .then(
                literal("apply").then(
                    argument("kosmogliph", IdentifierArgumentType.identifier()).suggests { ctx, builder ->
                        Kosmogliph.REGISTRY.keys
                            .map { it.value.toString() }
                            .forEach(builder::suggest)

                        builder.buildFuture()
                    }.executes(::cmd)
                )
            ).build()

        node.addChild(cmdNode)
    }

    fun cmd(ctx: CommandContext<ServerCommandSource>): Int {
        val id = IdentifierArgumentType.getIdentifier(ctx, "kosmogliph")
        val kosmogliph = Kosmogliph.REGISTRY.get(id) ?: return -1
        val player = ctx.source.player ?: return -1
        if (!kosmogliph.canBeAppliedTo(player.mainHandStack)) return -1
        val result = Kosmogliph.addToComponent(player.mainHandStack, kosmogliph)

        if (result.isLeft()) return -1

        return Command.SINGLE_SUCCESS
    }
}