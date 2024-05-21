package us.timinc.mc.cobblemon.counter.command.ko

import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.KoApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object KoTotalCommand : PlayerCommandExecutor() {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val score = KoApi.getTotal(player)
        ctx.source.sendMessage(Text.translatable("counter.ko.total", player.displayName, score))
        return score
    }
}