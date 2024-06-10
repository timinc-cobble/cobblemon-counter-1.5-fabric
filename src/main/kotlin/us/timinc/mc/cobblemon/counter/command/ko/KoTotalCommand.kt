package us.timinc.mc.cobblemon.counter.command.ko

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.KoApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object KoTotalCommand : PlayerCommandExecutor(listOf("ko", "total")) {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("counter").then(
                literal("ko").then(
                    literal("total")
                        .then(
                            argument("player", EntityArgumentType.player())
                                .executes { withPlayer(it) }
                        )
                        .executes { withoutPlayer(it) }
                )
            )
        )
    }

    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val score = KoApi.getTotal(player)
        ctx.source.sendMessage(Text.translatable("counter.ko.total", player.displayName, score))
        return score
    }
}