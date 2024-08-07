package us.timinc.mc.cobblemon.counter.command.ko

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.KoApi
import us.timinc.mc.cobblemon.counter.command.PlayerCommandExecutor

object KoStreakCommand : PlayerCommandExecutor(listOf("ko", "streak")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        val streakData = KoApi.getStreak(player)
        val species = PokemonSpecies.getByName(streakData.first) ?: return 0
        val count = streakData.second
        ctx.source.sendMessage(Text.translatable("counter.ko.streak", player.displayName, count, species))
        return count
    }
}