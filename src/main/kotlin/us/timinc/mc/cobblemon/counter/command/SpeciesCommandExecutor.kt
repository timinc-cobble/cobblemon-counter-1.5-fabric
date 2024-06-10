package us.timinc.mc.cobblemon.counter.command

import com.cobblemon.mod.common.command.argument.PokemonArgumentType
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

abstract class SpeciesCommandExecutor(private val path: List<String>) : CommandExecutor() {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val reversedPath = path.reversed()
        var lastLink = literal(reversedPath.first())
            .then(
                argument("species", PokemonArgumentType.pokemon())
                    .then(
                        argument("player", EntityArgumentType.player())
                            .executes(::withPlayer)
                    )
                    .executes(::withoutPlayer)
            )

        for (name in reversedPath.drop(1)) {
            val nextLink = literal(name)
            nextLink.then(lastLink)
            lastLink = nextLink
        }

        dispatcher.register(literal("counter").then(lastLink))
    }

    fun withPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return check(
            ctx,
            EntityArgumentType.getPlayer(ctx, "player"),
            PokemonArgumentType.getPokemon(ctx, "species").name.lowercase()
        )
    }

    fun withoutPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return ctx.source.player?.let { player ->
            check(
                ctx, player, PokemonArgumentType.getPokemon(ctx, "species").name.lowercase()
            )
        } ?: 0
    }

    abstract fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity, species: String): Int
}