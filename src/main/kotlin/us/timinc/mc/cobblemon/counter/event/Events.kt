package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback

object Events {
    fun register() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, PokemonCapture::handle)
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, BattleFainted::handle)
        CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.NORMAL, PokemonCatchRate::handle)
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, BattleStartedPost::handle)
        CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, EvolutionComplete::handle)
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, StarterChosen::handle)
        CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, TradeCompleted::handle)
        UseEntityCallback.EVENT.register(UseEntity::handle)
    }
}