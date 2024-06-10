package us.timinc.mc.cobblemon.counter.command.capture

import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object CaptureTotalCommand : PlayerCommandExecutor(listOf("capture", "total")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val score = CaptureApi.getTotal(player)
        ctx.source.sendMessage(Text.translatable("counter.capture.total", player.displayName, score))
        return score
    }
}