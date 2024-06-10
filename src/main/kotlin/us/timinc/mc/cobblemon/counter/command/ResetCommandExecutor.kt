package us.timinc.mc.cobblemon.counter.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

abstract class ResetCommandExecutor(val name: String) : CommandExecutor() {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("counter").then(
                literal(name).then(
                    literal("reset")
                        .requires { source -> source.hasPermissionLevel(2) }
                        .then(
                            literal("count")
                                .then(
                                    argument("player", EntityArgumentType.player())
                                        .executes { resetCount(it) }
                                )
                        )
                        .then(
                            literal("streak")
                                .then(
                                    argument("player", EntityArgumentType.player())
                                        .executes { resetStreak(it) }
                                )
                        )
                        .then(
                            literal("all").then(
                                argument("player", EntityArgumentType.player())
                                    .executes { reset(it) }
                            )
                        )
                )
            )
        )
    }

    abstract fun resetCount(ctx: CommandContext<ServerCommandSource>): Int
    abstract fun resetStreak(ctx: CommandContext<ServerCommandSource>): Int
    abstract fun reset(ctx: CommandContext<ServerCommandSource>): Int
}