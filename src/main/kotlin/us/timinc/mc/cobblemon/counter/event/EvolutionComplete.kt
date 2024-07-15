package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.pokemon.evolution.EvolutionCompleteEvent
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.EncounterApi

object EvolutionComplete {
    fun handle(evt: EvolutionCompleteEvent) {
        val species = evt.pokemon.species
        val player = evt.pokemon.getOwnerPlayer() ?: return
        EncounterApi.add(player, species)
        CaptureApi.add(player, species, true)
    }
}