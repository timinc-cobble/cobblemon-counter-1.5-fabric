package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.battles.BattleStartedPostEvent
import com.cobblemon.mod.common.util.getPlayer
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import java.util.*

object BattleStartedPost {
    fun handle(event: BattleStartedPostEvent) {
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
}