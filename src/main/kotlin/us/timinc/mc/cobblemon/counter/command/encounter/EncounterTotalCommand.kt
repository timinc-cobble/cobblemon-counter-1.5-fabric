package us.timinc.mc.cobblemon.counter.command.encounter

import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object EncounterTotalCommand : PlayerCommandExecutor(listOf("encounter", "total")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val score = EncounterApi.getTotal(player)
        ctx.source.sendMessage(Text.translatable("counter.encounter.total", player.displayName, score))
        return score
    }
}