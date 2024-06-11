package us.timinc.mc.cobblemon.counter.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import us.timinc.mc.cobblemon.counter.command.capture.CaptureCountCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureResetCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureStreakCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureTotalCommand
import us.timinc.mc.cobblemon.counter.command.encounter.EncounterCheckCommand
import us.timinc.mc.cobblemon.counter.command.encounter.EncounterResetCommand
import us.timinc.mc.cobblemon.counter.command.encounter.EncounterTotalCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoCountCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoResetCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoStreakCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoTotalCommand

object Commands {
    fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registry: CommandRegistryAccess,
        registrationEnvironment: CommandManager.RegistrationEnvironment
    ) {
        KoCountCommand.register(dispatcher)
        KoResetCommand.register(dispatcher)
        KoStreakCommand.register(dispatcher)
        KoTotalCommand.register(dispatcher)

        CaptureCountCommand.register(dispatcher)
        CaptureResetCommand.register(dispatcher)
        CaptureStreakCommand.register(dispatcher)
        CaptureTotalCommand.register(dispatcher)

        EncounterCheckCommand.register(dispatcher)
        EncounterResetCommand.register(dispatcher)
        EncounterTotalCommand.register(dispatcher)
    }
}