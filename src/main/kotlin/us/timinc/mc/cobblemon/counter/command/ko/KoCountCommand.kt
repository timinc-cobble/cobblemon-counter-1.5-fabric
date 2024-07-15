package us.timinc.mc.cobblemon.counter.command.ko

import com.cobblemon.mod.common.pokemon.Species
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.KoApi
import us.timinc.mc.cobblemon.counter.command.SpeciesCommandExecutor

object KoCountCommand : SpeciesCommandExecutor(listOf("ko", "count")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity, species: Species): Int {
        val score = KoApi.getCount(player, species)
        ctx.source.sendMessage(Text.translatable("counter.ko.count", player.displayName, score, species.translatedName))
        return score
    }
}