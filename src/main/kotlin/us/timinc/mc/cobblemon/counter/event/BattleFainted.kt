package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.util.getPlayer
import us.timinc.mc.cobblemon.counter.api.KoApi
import java.util.*

object BattleFainted {
    fun handle(event: BattleFaintedEvent) {
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
}