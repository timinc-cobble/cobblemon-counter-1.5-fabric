package us.timinc.mc.cobblemon.counter.command.encounter

import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import us.timinc.mc.cobblemon.counter.command.SpeciesCommandExecutor

object EncounterCheckCommand : SpeciesCommandExecutor(listOf("encounter", "check")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity, species: String): Int {
        val check = EncounterApi.check(player, species)
        ctx.source.sendMessage(Text.translatable("counter.encounter.check" + (if (check) ".positive" else ".negative"), player.displayName, species))
        return if (check) 1 else 0
    }
}