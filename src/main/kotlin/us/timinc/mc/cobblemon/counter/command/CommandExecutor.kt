package us.timinc.mc.cobblemon.counter.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource

abstract class CommandExecutor {
    abstract fun register(dispatcher: CommandDispatcher<ServerCommandSource>)
}