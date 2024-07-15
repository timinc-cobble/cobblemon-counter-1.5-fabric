package us.timinc.mc.cobblemon.counter.command.encounter

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import us.timinc.mc.cobblemon.counter.command.CommandExecutor

object EncounterResetCommand : CommandExecutor() {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("counter").then(
                literal("encounter").then(
                    literal("reset").requires { source ->
                        source.hasPermissionLevel(
                            2
                        )
                    }.then(
                        argument("player", EntityArgumentType.player()).executes(::reset)
                    )
                )
            )
        )
    }

    fun reset(ctx: CommandContext<ServerCommandSource>): Int {
        val target = EntityArgumentType.getPlayer(ctx, "player")
        EncounterApi.reset(target)
        ctx.source.sendMessage(Text.translatable("counter.encounter.all.reset", target.name))
        return 0
    }
}