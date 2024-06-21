@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.counter.api

import com.cobblemon.mod.common.Cobblemon
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.Counter.config
import us.timinc.mc.cobblemon.counter.Counter.info
import us.timinc.mc.cobblemon.counter.store.Encounter
import us.timinc.mc.cobblemon.counter.util.Util

object EncounterApi {
    fun add(player: PlayerEntity, species: String) {
        if (check(player, species)) {
            return
        }

        val playerData = Cobblemon.playerData.get(player)
        val encounter: Encounter = playerData.extraData.getOrPut(Encounter.NAME) { Encounter() } as Encounter
        encounter.add(species)
        Cobblemon.playerData.saveSingle(playerData)

        info("Player ${player.displayName.string} encountered a $species")
        if (config.broadcastEncountersToPlayer) {
            player.sendMessage(Text.translatable("counter.encounter.confirm", Text.translatable("cobblemon.species.${Util.cleanSpeciesNameForTranslation(species)}.name")))
        }
    }

    fun getTotal(player: PlayerEntity): Int {
        val playerData = Cobblemon.playerData.get(player)
        val encounter: Encounter = playerData.extraData.getOrPut(Encounter.NAME) { Encounter() } as Encounter
        return encounter.total()
    }

    fun check(player: PlayerEntity, species: String): Boolean {
        val playerData = Cobblemon.playerData.get(player)
        val encounter: Encounter = playerData.extraData.getOrPut(Encounter.NAME) { Encounter() } as Encounter
        return encounter.get(species)
    }

    fun reset(player: PlayerEntity) {
        val playerData = Cobblemon.playerData.get(player)
        val encounter: Encounter = playerData.extraData.getOrPut(Encounter.NAME) { Encounter() } as Encounter
        encounter.reset()
    }
}