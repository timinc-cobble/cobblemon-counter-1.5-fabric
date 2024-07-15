package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.pokemon.TradeCompletedEvent
import com.cobblemon.mod.common.util.server
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import java.util.*

object TradeCompleted {
    fun handle(evt: TradeCompletedEvent) {
        val species1 = evt.tradeParticipant1Pokemon.species
        val species2 = evt.tradeParticipant2Pokemon.species
        val player1 = server()?.playerManager?.getPlayer(evt.tradeParticipant1.uuid)
        val player2 = server()?.playerManager?.getPlayer(evt.tradeParticipant2.uuid)
        if (player1 != null) {
            EncounterApi.add(player1, species1)
            CaptureApi.add(player1, species1, true)
        }
        if (player2 != null) {
            EncounterApi.add(player2, species2)
            CaptureApi.add(player2, species2, true)
        }
    }
}