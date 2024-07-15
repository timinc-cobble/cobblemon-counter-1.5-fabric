package us.timinc.mc.cobblemon.counter.command.capture

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object CaptureStreakCommand : PlayerCommandExecutor(listOf("capture", "streak")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val streakData = CaptureApi.getStreak(player)
        val species = PokemonSpecies.getByName(streakData.first) ?: return 0
        val count = streakData.second
        ctx.source.sendMessage(Text.translatable("counter.capture.streak", player.displayName, count, species.translatedName))
        return count
    }
}