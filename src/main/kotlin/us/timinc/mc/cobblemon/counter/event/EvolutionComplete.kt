package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.pokemon.evolution.EvolutionCompleteEvent
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import java.util.*

object EvolutionComplete {
    fun handle(evt: EvolutionCompleteEvent) {
        val species = evt.pokemon.species.name.lowercase(Locale.getDefault())
        val player = evt.pokemon.getOwnerPlayer() ?: return
        EncounterApi.add(player, species)
    }
}