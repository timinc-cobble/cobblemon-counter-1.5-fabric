package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import java.util.*

object StarterChosen {
    fun handle(evt: StarterChosenEvent) {
        val species = evt.pokemon.species.name.lowercase(Locale.getDefault())
        val player = evt.player
        EncounterApi.add(player, species)
        CaptureApi.add(player, species, true)
    }
}