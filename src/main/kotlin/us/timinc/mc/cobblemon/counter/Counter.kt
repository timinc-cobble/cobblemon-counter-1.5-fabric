package us.timinc.mc.cobblemon.counter

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.battles.BattleStartedPostEvent
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry
import com.cobblemon.mod.common.command.argument.PokemonArgumentType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.server
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import us.timinc.mc.cobblemon.counter.api.KoApi
import us.timinc.mc.cobblemon.counter.command.capture.CaptureCountCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureResetCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureStreakCommand
import us.timinc.mc.cobblemon.counter.command.capture.CaptureTotalCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoCountCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoResetCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoStreakCommand
import us.timinc.mc.cobblemon.counter.command.ko.KoTotalCommand
import us.timinc.mc.cobblemon.counter.config.ConfigBuilder
import us.timinc.mc.cobblemon.counter.config.CounterConfig
import us.timinc.mc.cobblemon.counter.store.*
import java.util.*

object Counter : ModInitializer {
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    const val MOD_ID = "cobbled_counter"

    private var logger: Logger = LogManager.getLogger(MOD_ID)
    var config: CounterConfig = ConfigBuilder.load(CounterConfig::class.java, MOD_ID)

    override fun onInitialize() {
        PlayerDataExtensionRegistry.register(KoCount.NAME, KoCount::class.java)
        PlayerDataExtensionRegistry.register(KoStreak.NAME, KoStreak::class.java)
        PlayerDataExtensionRegistry.register(CaptureCount.NAME, CaptureCount::class.java)
        PlayerDataExtensionRegistry.register(CaptureStreak.NAME, CaptureStreak::class.java)
        PlayerDataExtensionRegistry.register(Encounter.NAME, Encounter::class.java)

        CobblemonEvents.POKEMON_CAPTURED.subscribe { handlePokemonCapture(it) }
        CobblemonEvents.BATTLE_FAINTED.subscribe { handleWildDefeat(it) }
        CobblemonEvents.POKEMON_CATCH_RATE.subscribe { repeatBallBooster(it) }
        CobblemonEvents.BATTLE_STARTED_POST.subscribe { handlePokemonEncounter(it) }
        CobblemonEvents.EVOLUTION_COMPLETE.subscribe { evt ->
            val species = evt.pokemon.species.name.toLowerCase()
            val player = evt.pokemon.getOwnerPlayer() ?: return@subscribe
            EncounterApi.add(player, species)
        }
        CobblemonEvents.STARTER_CHOSEN.subscribe { evt ->
            val species = evt.pokemon.species.name.toLowerCase()
            val player = evt.player
            EncounterApi.add(player, species)
            CaptureApi.add(player, species)
        }
        CobblemonEvents.TRADE_COMPLETED.subscribe { evt ->
            val species1 = evt.tradeParticipant1Pokemon.species.name.toLowerCase()
            val species2 = evt.tradeParticipant2Pokemon.species.name.toLowerCase()
            val player1 = server()?.playerManager?.getPlayer(evt.tradeParticipant1.uuid)
            val player2 = server()?.playerManager?.getPlayer(evt.tradeParticipant2.uuid)
            if (player1 != null) {
                EncounterApi.add(player1, species1)
            }
            if (player2 != null) {
                EncounterApi.add(player2, species2)
            }
        }
        UseEntityCallback.EVENT.register { player, world, hand, entity, _ ->
            if (entity !is PokemonEntity) return@register ActionResult.PASS
            if (!entity.pokemon.isWild()) {
                return@register ActionResult.PASS
            }
            if (!player.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty) {
                return@register ActionResult.PASS
            }
            if (!world.isClient) {
                val species = entity.pokemon.species.name.toLowerCase()
                val alreadyEncountered = EncounterApi.check(player, species)
                if (alreadyEncountered) {
                    if (config.broadcastEncountersToPlayer) {
                        val alreadyCaught = CaptureApi.getCount(player, species) > 0
                        player.sendMessage(
                            Text.translatable(
                                if (alreadyCaught) "counter.encounter.alreadyCaught" else "counter.encounter.alreadyEncountered",
                                species
                            )
                        )
                    }
                } else {
                    EncounterApi.add(player, species)
                }
            }
            return@register ActionResult.SUCCESS
        }
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal("counter").then(
                    literal("ko").then(
                        literal("count")
                            .then(
                                argument("species", PokemonArgumentType.pokemon())
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { KoCountCommand.withPlayer(it) }
                                    )
                                    .executes { KoCountCommand.withoutPlayer(it) }
                            )
                    ).then(
                        literal("streak")
                            .then(
                                argument("player", EntityArgumentType.player())
                                    .executes { KoStreakCommand.withPlayer(it) }
                            )
                            .executes { KoStreakCommand.withoutPlayer(it) }

                    ).then(
                        literal("total")
                            .then(
                                argument("player", EntityArgumentType.player())
                                    .executes { KoTotalCommand.withPlayer(it) }
                            )
                            .executes { KoTotalCommand.withoutPlayer(it) }
                    ).then(
                        literal("reset")
                            .requires { source -> source.hasPermissionLevel(2) }
                            .then(
                                literal("count")
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { KoResetCommand.resetCount(it) }
                                    )
                            )
                            .then(
                                literal("streak")
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { KoResetCommand.resetStreak(it) }
                                    )
                            )
                            .then(
                                literal("all").then(
                                    argument("player", EntityArgumentType.player())
                                        .executes { KoResetCommand.reset(it) }
                                )
                            )
                    )
                ).then(
                    literal("capture").then(
                        literal("count")
                            .then(
                                argument("species", PokemonArgumentType.pokemon())
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { CaptureCountCommand.withPlayer(it) }
                                    )
                                    .executes { CaptureCountCommand.withoutPlayer(it) }
                            )
                    ).then(
                        literal("streak")
                            .then(
                                argument("player", EntityArgumentType.player())
                                    .executes { CaptureStreakCommand.withPlayer(it) }
                            )
                            .executes { CaptureStreakCommand.withoutPlayer(it) }
                    ).then(
                        literal("total")
                            .then(
                                argument("player", EntityArgumentType.player())
                                    .executes { CaptureTotalCommand.withPlayer(it) }
                            )
                            .executes { CaptureTotalCommand.withoutPlayer(it) }
                    ).then(
                        literal("reset")
                            .requires { source -> source.hasPermissionLevel(2) }
                            .then(
                                literal("count")
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { CaptureResetCommand.resetCount(it) }
                                    )
                            )
                            .then(
                                literal("streak")
                                    .then(
                                        argument("player", EntityArgumentType.player())
                                            .executes { CaptureResetCommand.resetStreak(it) }
                                    )
                            )
                            .then(
                                literal("all").then(
                                    argument("player", EntityArgumentType.player())
                                        .executes { CaptureResetCommand.reset(it) }
                                )
                            )
                    )
                )
            )
        }
    }

    private fun repeatBallBooster(event: PokemonCatchRateEvent) {
        val thrower = event.thrower

        if (thrower !is ServerPlayerEntity) return

        if (event.pokeBallEntity.pokeBall == PokeBalls.REPEAT_BALL && CaptureApi.getCount(
                thrower, event.pokemonEntity.pokemon.species.name.lowercase()
            ) > 0
        ) {
            event.catchRate *= 2.5f
        }
    }

    private fun handlePokemonEncounter(event: BattleStartedPostEvent) {
        val actors = event.battle.actors
        val wildActors = actors.filter { it.type === ActorType.WILD }
        if (wildActors.isEmpty()) return
        val playerActors = actors.filter { it.type === ActorType.PLAYER }
        if (playerActors.isEmpty()) return

        val encounteredWilds =
            wildActors.flatMap { actor -> actor.pokemonList.map { it.originalPokemon.species.name.lowercase() } }
        val players = playerActors.flatMap { it.getPlayerUUIDs() }.mapNotNull(UUID::getPlayer)

        players.forEach { player ->
            encounteredWilds.forEach { species ->
                EncounterApi.add(player, species)
            }
        }
    }

    private fun handlePokemonCapture(event: PokemonCapturedEvent) {
        val species = event.pokemon.species.name.lowercase()
        CaptureApi.add(event.player, species)
    }

    private fun handleWildDefeat(event: BattleFaintedEvent) {
        val targetEntity = event.killed.entity ?: return
        val targetPokemon = targetEntity.pokemon
        if (!targetPokemon.isWild()) {
            return
        }
        val species = targetPokemon.species.name.lowercase()

        event.battle.playerUUIDs.mapNotNull(UUID::getPlayer).forEach { player ->
            KoApi.add(player, species)
        }
    }

    fun info(msg: String) {
        if (!config.debug) return
        logger.info(msg)
    }
}