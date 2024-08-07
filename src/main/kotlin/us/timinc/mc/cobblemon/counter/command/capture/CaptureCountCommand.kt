package us.timinc.mc.cobblemon.counter.command.capture

import com.cobblemon.mod.common.pokemon.Species
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.command.SpeciesCommandExecutor

object CaptureCountCommand : SpeciesCommandExecutor(listOf("capture", "count")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity, species: Species): Int {
        val score = CaptureApi.getCount(player, species)
        ctx.source.sendMessage(Text.translatable("counter.capture.count", player.displayName, score, species.translatedName))
        return score
    }
}