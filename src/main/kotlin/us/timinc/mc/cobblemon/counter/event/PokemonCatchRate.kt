package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import net.minecraft.server.network.ServerPlayerEntity
import us.timinc.mc.cobblemon.counter.api.CaptureApi

object PokemonCatchRate {
    fun handle(event: PokemonCatchRateEvent) {
        val thrower = event.thrower

        if (thrower !is ServerPlayerEntity) return

        if (event.pokeBallEntity.pokeBall == PokeBalls.REPEAT_BALL && CaptureApi.getCount(
                thrower, event.pokemonEntity.pokemon.species
            ) > 0
        ) {
            event.catchRate *= 2.5f
        }
    }
}